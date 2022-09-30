package bottomtextdanny.braincell.libraries.chart.steppy;

import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.libraries.chart.steppy.data.Data;
import bottomtextdanny.braincell.libraries.chart.steppy.data.StepType;
import bottomtextdanny.braincell.libraries.chart.steppy.data.Type;
import bottomtextdanny.braincell.libraries.chart.steppy.iteration.StepDataType;
import bottomtextdanny.braincell.libraries.chart.steppy.iteration.TransientData;
import bottomtextdanny.braincell.libraries.chart.steppy.step.Step;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import net.minecraft.util.RandomSource;

import java.util.LinkedList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class UtilitySteps {
    private static final RandomSource RANDOM = RandomSource.create();

    @StepType(Type.WRITER)
    public record Write<T>(StepDataType<T> key, Step valueGetter) implements Step {

        @SuppressWarnings("unchecked")
        @Override
        public Object invoke(Data data, ObjectFetcher metadata, TransientData preceding) {
            preceding.put(key, (T) valueGetter.invoke(data, metadata, preceding));
            return null;
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.ROOT)
    public record GetTransient<T>(StepDataType<T> key) implements Step {

        @Override
        public Object invoke(Data data, ObjectFetcher metadata, TransientData preceding) {
            return preceding.getOrDefault(key);
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.CONTEXT_FUNCTION)
    public record Virtual(Step step) implements Step {

        @Override
        public Object invoke(Data data, ObjectFetcher metadata, TransientData preceding) {
            preceding.uploadCurrentLayerToHistory();
            int prevOps = preceding.getHistoryPointer();
            Object invoke = step.invoke(data, metadata, preceding);
            preceding.resetToHistory(preceding.getHistoryPointer() - prevOps);
            return invoke;
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.CONTEXT_FUNCTION)
    public record Concat2(Step step1, Step step2) implements Step {

        @Override
        public Object invoke(Data data, ObjectFetcher metadata, TransientData preceding) {
            step1.invoke(data, metadata, preceding);
            return step2.invoke(data, metadata, preceding);
        }

        @Override
        public Concat3 append(Step other) {
            return new Concat3(step1, step2, other);
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.CONTEXT_FUNCTION)
    public record Concat3(Step step1, Step step2, Step step3) implements Step {

        @Override
        public Object invoke(Data data, ObjectFetcher metadata, TransientData preceding) {
            step1.invoke(data, metadata, preceding);
            step2.invoke(data, metadata, preceding);
            return step3.invoke(data, metadata, preceding);
        }

        @Override
        public Concat append(Step other) {
            UtilitySteps.Concat concat = new UtilitySteps.Concat();
            concat.append(step1);
            concat.append(step2);
            concat.append(step3);
            concat.append(other);
            return concat;
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.CONTEXT_FUNCTION)
    public static class Concat implements Step {
        private final List<Step> steps = new LinkedList<>();

        public Concat append(Step step) {
            steps.add(step);
            return this;
        }

        @Override
        public Object invoke(Data data, ObjectFetcher metadata, TransientData preceding) {
            Object r = null;
            for (Step operation : steps) {
                r = operation.invoke(data, metadata, preceding);
            }

            return r;
        }

        public void iterate(Consumer<Step> consumer) {
            steps.forEach(consumer);
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.CONTEXT_FUNCTION)
    public record MetadataStep(int index) implements Step {

        @Override
        public Object invoke(Data data, ObjectFetcher metadata, TransientData preceding) {
            Object o = metadata.get(index);

            if (o instanceof Step) return ((Step) o).invoke(data, metadata, preceding);
            return null;
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.ROOT)
    public record Metadata(int index) implements Step {

        @Override
        public Object invoke(Data data, ObjectFetcher metadata, TransientData preceding) {
            return metadata.get(index);
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.ROOT)
    public record Null() implements Step {
        public static final Null INSTANCE = new Null();

        @Override
        public Object invoke(Data data, ObjectFetcher metadata, TransientData preceding) {
            return null;
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.CONTEXT_FUNCTION)
    public record If(Step conditional, Step outcome) implements Step {

        @Override
        public Object invoke(Data data, ObjectFetcher metadata, TransientData preceding) {
            if (conditional.invokeCast(data, metadata, preceding, false))
                return outcome.invoke(data, metadata, preceding);
            return null;
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.CONTEXT_FUNCTION)
    public record Either(Step conditional,
                         Step positiveOutcome,
                         Step negativeOutcome) implements Step {

        @Override
        public Object invoke(Data data, ObjectFetcher metadata, TransientData preceding) {
            if (conditional.invokeCast(data, metadata, preceding, false))
                return positiveOutcome.invoke(data, metadata, preceding);
            return negativeOutcome.invoke(data, metadata, preceding);
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.ROOT)
    public record Indices(List<Step> steps) implements Step {

        @Override
        public Object invoke(Data data, ObjectFetcher metadata, TransientData preceding) {
            return steps;
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.CONTEXT_FUNCTION)
    public static class ChooseByWeight implements Step {
        private final NavigableMap<Float, Step> weightTable = new TreeMap<>();
        private float total;

        @Override
        public Object invoke(Data data, ObjectFetcher metadata, TransientData preceding) {
            return weightTable.higherEntry(total * RANDOM.nextFloat()).getValue()
                    .invoke(data, metadata, preceding);
        }

        public ChooseByWeight entry(float weight, Step step) {
            if (weight <= 0) return this;

            total += weight;
            weightTable.put(total, step);

            return this;
        }

        public void iterate(BiConsumer<Float, Step> consumer) {
            weightTable.forEach(consumer);
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.CONTEXT_FUNCTION)
    public record Picker(Step indices, Step indexNumber) implements Step {

        @Override
        public Object invoke(Data data, ObjectFetcher metadata, TransientData preceding) {
            if (indices.invoke(data, metadata, preceding) instanceof List<?> list) {
                Integer index = indexNumber.invokeCast(data, metadata, preceding, 0);
                if (index >= 0 && index < list.size() && list.get(index) instanceof Step step) {
                    return step.invoke(data, metadata, preceding);
                }
            }
            return preceding;
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }
}
