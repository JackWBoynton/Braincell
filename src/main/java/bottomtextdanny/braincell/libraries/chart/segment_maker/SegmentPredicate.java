package bottomtextdanny.braincell.libraries.chart.segment_maker;

import bottomtextdanny.braincell.libraries.chart.segment.SegmentData;
import bottomtextdanny.braincell.libraries.registry.Serializable;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;

public interface SegmentPredicate extends Serializable {

    boolean test(SegmentData data);

    default SegmentPredicates.Concat spAppend(SegmentPredicate predicate) {
        SegmentPredicates.Concat list = new SegmentPredicates.Concat();
        list.spAppend(this);
        list.spAppend(predicate);
        return list;
    }

    default SegmentPredicates.ConcatOred spOr(SegmentPredicate predicate) {
        SegmentPredicates.ConcatOred list = new SegmentPredicates.ConcatOred();
        list.spOr(this);
        list.spOr(predicate);
        return list;
    }

    default SegmentPredicates.Either spEither(SegmentPredicate positive,
                                              SegmentPredicate negative) {
        return new SegmentPredicates.Either(this, positive, negative);
    }

    default SegmentPredicates.Negate spNegate() {
        return new SegmentPredicates.Negate(this);
    }

    @Override
    default Wrap<? extends Serializer<? extends SegmentPredicate>> serializer() {
        return null;
    }
}
