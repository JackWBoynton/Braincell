package bottomtextdanny.braincell.libraries.chart.steppy.step;

import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.libraries.chart.steppy.data.Data;
import bottomtextdanny.braincell.libraries.chart.steppy.iteration.TransientData;

public interface IntStep extends NumberStep {

    @Override
    default Integer invoke(Data data, ObjectFetcher metadata, TransientData preceding) {
        return invokeInt(data, metadata, preceding);
    }

    @Override
    default byte invokeByte(Data data, ObjectFetcher metadata, TransientData preceding) {
        return (byte) invokeInt(data, metadata, preceding);
    }

    @Override
    default short invokeShort(Data data, ObjectFetcher metadata, TransientData preceding) {
        return (short) invokeInt(data, metadata, preceding);
    }

    @Override
    default float invokeFloat(Data data, ObjectFetcher metadata, TransientData preceding) {
        return (float) invokeInt(data, metadata, preceding);
    }

    @Override
    default double invokeDouble(Data data, ObjectFetcher metadata, TransientData preceding) {
        return invokeInt(data, metadata, preceding);
    }

    @Override
    default long invokeLong(Data data, ObjectFetcher metadata, TransientData preceding) {
        return (long) invokeInt(data, metadata, preceding);
    }
}
