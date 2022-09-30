package bottomtextdanny.braincell.libraries.chart.steppy;

import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.libraries.chart.help.GridSampler;
import bottomtextdanny.braincell.libraries.chart.steppy.data.Data;
import bottomtextdanny.braincell.libraries.chart.steppy.data.StepType;
import bottomtextdanny.braincell.libraries.chart.steppy.data.Type;
import bottomtextdanny.braincell.libraries.chart.steppy.iteration.TransientData;
import bottomtextdanny.braincell.libraries.chart.steppy.step.*;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import bottomtextdanny.braincell.tables.BCStepDataTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class NumberSteps {
    private static final RandomSource RANDOM = RandomSource.create(69L);

    @StepType(Type.CONTEXT_FUNCTION)
    public record Addition(Step addition) implements FloatStep {

        @Override
        public float invokeFloat(Data data, ObjectFetcher metadata, TransientData preceding) {
            return (float) preceding.getOrDefault(BCStepDataTypes.NUMBER.get()) + addition.invokeCast(data, metadata, preceding, 0.0F);
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.CONTEXT_FUNCTION)
    public record Scale(Step scalar) implements FloatStep {

        @Override
        public float invokeFloat(Data data, ObjectFetcher metadata, TransientData preceding) {
            return (float) preceding.getOrDefault(BCStepDataTypes.NUMBER.get()) * scalar.invokeCast(data, metadata, preceding, 0.0F);
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.CONTEXT_FUNCTION)
    public record Mod(Step scalar) implements FloatStep {

        @Override
        public float invokeFloat(Data data, ObjectFetcher metadata, TransientData preceding) {
            return (float) preceding.getOrDefault(BCStepDataTypes.NUMBER.get()) % scalar.invokeCast(data, metadata, preceding, 0.0F);
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.CONTEXT_FUNCTION)
    public record Sample(GridSampler sampler) implements FloatStep {

        @Override
        public float invokeFloat(Data data, ObjectFetcher metadata, TransientData preceding) {
            return sampler.value(preceding.getOrDefault(BCStepDataTypes.VEC3.get()));
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.CONTEXT_FUNCTION)
    public record RandomNumber(Step minNumber, Step maxNumber) implements FloatStep {

        @Override
        public float invokeFloat(Data data, ObjectFetcher metadata, TransientData preceding) {
            float minValue = minNumber.invokeCast(data, metadata, preceding, 0.0F);
            float range = maxNumber.invokeCast(data, metadata, preceding, minValue) - minValue;
            return minValue + RANDOM.nextFloat() * range;
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.ROOT)
    public record FixedFloat(float value) implements FloatStep {

        @Override
        public float invokeFloat(Data data, ObjectFetcher metadata, TransientData preceding) {
            return value;
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.ROOT)
    public record FixedInt(int value) implements IntStep {

        @Override
        public int invokeInt(Data data, ObjectFetcher metadata, TransientData preceding) {
            return value;
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }
}
