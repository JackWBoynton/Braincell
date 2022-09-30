package bottomtextdanny.braincell.libraries.chart.steppy.step;

import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.libraries.chart.steppy.UtilitySteps;
import bottomtextdanny.braincell.libraries.chart.steppy.data.Data;
import bottomtextdanny.braincell.libraries.chart.steppy.iteration.TransientData;
import bottomtextdanny.braincell.libraries.registry.Serializable;

public interface Step extends Serializable {

    Object invoke(Data data, ObjectFetcher metadata, TransientData preceding);

    @SuppressWarnings("unchecked")
    default <T> T invokeCast(Data data, ObjectFetcher metadata, TransientData preceding, T defaultValue) {
        Object invoke = invoke(data, metadata, preceding);

        if (invoke == null) {
            return defaultValue;
        }
        return (T) invoke;
    }

    default Step append(Step other) {
        return new UtilitySteps.Concat2(this, other);
    }
}
