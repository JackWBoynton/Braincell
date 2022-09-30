package bottomtextdanny.braincell.libraries.chart.steppy;

import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.libraries.chart.steppy.data.Data;
import bottomtextdanny.braincell.libraries.chart.steppy.data.StepType;
import bottomtextdanny.braincell.libraries.chart.steppy.data.Type;
import bottomtextdanny.braincell.libraries.chart.steppy.iteration.TransientData;
import bottomtextdanny.braincell.libraries.chart.steppy.step.Step;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;

public final class VoidSteps {

    @StepType(Type.CONTEXT_FUNCTION)
    public record Place(Step step1, Step step2) implements Step {

        @Override
        public Object invoke(Data data, ObjectFetcher metadata, TransientData preceding) {
            step1.invoke(data, metadata, preceding);
            return step2.invoke(data, metadata, preceding);
        }

        @Override
        public UtilitySteps.Concat3 append(Step other) {
            return new UtilitySteps.Concat3(step1, step2, other);
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }
}
