package bottomtextdanny.braincell.libraries.chart.steppy.step;

import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.libraries.chart.steppy.data.Data;
import bottomtextdanny.braincell.libraries.chart.steppy.iteration.TransientData;

public interface NumberStep extends Step {

    @Override
    Number invoke(Data data, ObjectFetcher metadata, TransientData preceding);

    byte invokeByte(Data data, ObjectFetcher metadata, TransientData preceding);

    short invokeShort(Data data, ObjectFetcher metadata, TransientData preceding);

    int invokeInt(Data data, ObjectFetcher metadata, TransientData preceding);

    float invokeFloat(Data data, ObjectFetcher metadata, TransientData preceding);

    double invokeDouble(Data data, ObjectFetcher metadata, TransientData preceding);

    long invokeLong(Data data, ObjectFetcher metadata, TransientData preceding);
}
