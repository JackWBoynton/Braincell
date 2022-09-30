package bottomtextdanny.braincell.libraries.chart.segment_maker;

import bottomtextdanny.braincell.libraries.chart.segment.SegmentData;
import bottomtextdanny.braincell.libraries.registry.Serializable;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;

public interface SegmentFloat extends Serializable {

    float process(SegmentData data, float base);

    @Override
    default Wrap<? extends Serializer<?>> serializer() {
        return null;
    }
}
