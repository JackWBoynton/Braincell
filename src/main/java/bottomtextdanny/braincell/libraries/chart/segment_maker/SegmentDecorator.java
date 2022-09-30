package bottomtextdanny.braincell.libraries.chart.segment_maker;

import bottomtextdanny.braincell.libraries.chart.segment.SegmentData;
import bottomtextdanny.braincell.libraries.registry.Serializable;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;

public interface SegmentDecorator extends Serializable {

    void accept(SegmentData data);

    default SegmentDecorators.Concat append(SegmentDecorator predicate) {
        SegmentDecorators.Concat list = new SegmentDecorators.Concat();
        list.append(this);
        list.append(predicate);
        return list;
    }

    @Override
    default Wrap<? extends Serializer<? extends SegmentDecorator>> serializer() {
        return null;
    }
}
