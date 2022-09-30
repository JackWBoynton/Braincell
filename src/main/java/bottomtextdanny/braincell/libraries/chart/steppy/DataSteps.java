package bottomtextdanny.braincell.libraries.chart.steppy;

import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.libraries.chart.steppy.data.*;
import bottomtextdanny.braincell.libraries.chart.steppy.iteration.TransientData;
import bottomtextdanny.braincell.libraries.chart.steppy.step.*;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;

public final class DataSteps {

    @StepType(Type.ROOT)
    public record TagKey(net.minecraft.tags.TagKey<?> tagKey) implements TagKeyStep {

        @Override
        public net.minecraft.tags.TagKey<?> invokeTagKey(Data data, ObjectFetcher metadata, TransientData preceding) {
            return tagKey;
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }
}
