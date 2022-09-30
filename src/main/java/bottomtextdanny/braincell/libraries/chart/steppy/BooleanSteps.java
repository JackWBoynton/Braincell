package bottomtextdanny.braincell.libraries.chart.steppy;

import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.libraries.chart.steppy.data.Data;
import bottomtextdanny.braincell.libraries.chart.steppy.data.StepType;
import bottomtextdanny.braincell.libraries.chart.steppy.data.Type;
import bottomtextdanny.braincell.libraries.chart.steppy.iteration.TransientData;
import bottomtextdanny.braincell.libraries.chart.steppy.step.*;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import bottomtextdanny.braincell.tables.BCStepDataTypes;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public final class BooleanSteps {

    @StepType(Type.CONTEXT_FUNCTION)
    public record BooleanConcat2(boolean anyMatch, Step conditional1, Step conditional2) implements BooleanStep {

        @Override
        public boolean invokeBoolean(Data data, ObjectFetcher metadata, TransientData preceding) {
            if (anyMatch) {
                return conditional1.invokeCast(data, metadata, preceding, false)
                        || conditional2.invokeCast(data, metadata, preceding, false);
            }
            return conditional1.invokeCast(data, metadata, preceding, false)
                    && conditional2.invokeCast(data, metadata, preceding, false);
        }

        @Override
        public BooleanStep and(Step otherConditional) {
            return new BooleanConcat3(false, conditional1, conditional2, otherConditional);
        }

        @Override
        public BooleanStep or(Step otherConditional) {
            return new BooleanConcat3(true, conditional1, conditional2, otherConditional);
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.CONTEXT_FUNCTION)
    public record BooleanConcat3(boolean anyMatch, Step conditional1, Step conditional2, Step conditional3) implements BooleanStep {

        @Override
        public boolean invokeBoolean(Data data, ObjectFetcher metadata, TransientData preceding) {
            if (anyMatch) {
                return conditional1.invokeCast(data, metadata, preceding, false)
                        || conditional2.invokeCast(data, metadata, preceding, false)
                        || conditional3.invokeCast(data, metadata, preceding, false);
            }
            return conditional1.invokeCast(data, metadata, preceding, false)
                    && conditional2.invokeCast(data, metadata, preceding, false)
                    && conditional3.invokeCast(data, metadata, preceding, false);
        }

        @Override
        public BooleanStep and(Step otherConditional) {
            BooleanConcat concat = new BooleanConcat(false);
            concat.and(conditional1);
            concat.and(conditional2);
            concat.and(conditional3);
            concat.and(otherConditional);
            return concat;
        }

        @Override
        public BooleanStep or(Step otherConditional) {
            BooleanConcat concat = new BooleanConcat(true);
            concat.or(conditional1);
            concat.or(conditional2);
            concat.or(conditional3);
            concat.or(otherConditional);
            return concat;
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.CONTEXT_FUNCTION)
    public static class BooleanConcat implements BooleanStep {
        private final List<Step> conditionals = new LinkedList<>();
        public final boolean anyMatch;

        public BooleanConcat(boolean anyMatch) {
            super();
            this.anyMatch = anyMatch;
        }

        @Override
        public boolean invokeBoolean(Data data, ObjectFetcher metadata, TransientData preceding) {
            if (anyMatch) {
                for (Step conditional : conditionals)
                    if (conditional.invokeCast(data, metadata, preceding, false)) return true;
                return false;
            } else {
                for (Step conditional : conditionals)
                    if (!conditional.invokeCast(data, metadata, preceding, false)) return false;
                return true;
            }
        }

        @Override
        public BooleanStep and(Step otherConditional) {
            if (anyMatch) {
                return new BooleanConcat2(false, this, otherConditional);
            }

            conditionals.add(otherConditional);
            return this;
        }

        @Override
        public BooleanStep or(Step otherConditional) {
            if (anyMatch) {
                conditionals.add(otherConditional);
                return this;
            }

            return new BooleanConcat2(true, this, otherConditional);
        }

        public void iterate(Consumer<Step> consumer) {
            conditionals.forEach(consumer);
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.ROOT)
    public record True() implements BooleanStep {
        public static final True INSTANCE = new True();

        @Override
        public boolean invokeBoolean(Data data, ObjectFetcher metadata, TransientData preceding) {
            return true;
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.ROOT)
    public record False() implements BooleanStep {
        public static final False INSTANCE = new False();

        @Override
        public boolean invokeBoolean(Data data, ObjectFetcher metadata, TransientData preceding) {
            return false;
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.CONTEXT_FUNCTION)
    public record IsNegative() implements BooleanStep {
        public static final IsNegative INSTANCE = new IsNegative();

        @Override
        public boolean invokeBoolean(Data data, ObjectFetcher metadata, TransientData preceding) {
            return preceding.get(BCStepDataTypes.BOOL.get()).map(b -> !b).orElse(false);
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.ROOT)
    public record NegativeOf(Step conditional) implements BooleanStep {

        @Override
        public boolean invokeBoolean(Data data, ObjectFetcher metadata, TransientData preceding) {
            return !conditional.invokeCast(data, metadata, preceding, false);
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }
}
