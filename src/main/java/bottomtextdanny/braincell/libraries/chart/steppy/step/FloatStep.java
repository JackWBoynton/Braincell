package bottomtextdanny.braincell.libraries.chart.steppy.step;

import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.libraries.chart.steppy.data.Data;
import bottomtextdanny.braincell.libraries.chart.steppy.iteration.TransientData;

public interface FloatStep extends NumberStep {

    @Override
    default Float invoke(Data data, ObjectFetcher metadata, TransientData preceding) {
        return invokeFloat(data, metadata, preceding);
    }

    @Override
    default byte invokeByte(Data data, ObjectFetcher metadata, TransientData preceding) {
        return (byte) invokeFloat(data, metadata, preceding);
    }

    @Override
    default short invokeShort(Data data, ObjectFetcher metadata, TransientData preceding) {
        return (short) invokeFloat(data, metadata, preceding);
    }

    @Override
    default int invokeInt(Data data, ObjectFetcher metadata, TransientData preceding) {
        return (int) invokeFloat(data, metadata, preceding);
    }

    @Override
    default double invokeDouble(Data data, ObjectFetcher metadata, TransientData preceding) {
        return invokeFloat(data, metadata, preceding);
    }

    @Override
    default long invokeLong(Data data, ObjectFetcher metadata, TransientData preceding) {
        return (long) invokeFloat(data, metadata, preceding);
    }
}
