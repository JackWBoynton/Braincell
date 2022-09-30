package bottomtextdanny.braincell.libraries.chart.steppy.step;

import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.libraries.chart.steppy.BooleanSteps;
import bottomtextdanny.braincell.libraries.chart.steppy.data.Data;
import bottomtextdanny.braincell.libraries.chart.steppy.iteration.TransientData;

public interface BooleanStep extends Step {

    boolean invokeBoolean(Data data, ObjectFetcher metadata, TransientData preceding);

    default BooleanStep and(Step otherConditional) {
        return new BooleanSteps.BooleanConcat2(false, this, otherConditional);
    }

    default BooleanStep or(Step otherConditional) {
        return new BooleanSteps.BooleanConcat2(true, this, otherConditional);
    }

    default BooleanSteps.NegativeOf negative() {
        return new BooleanSteps.NegativeOf(this);
    }

    @Override
    default Object invoke(Data data, ObjectFetcher metadata, TransientData preceding) {
        return invokeBoolean(data, metadata, preceding);
    }
}
