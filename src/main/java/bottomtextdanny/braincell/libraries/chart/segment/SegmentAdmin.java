package bottomtextdanny.braincell.libraries.chart.segment;

import bottomtextdanny.braincell.libraries.chart.segment_maker.SegmentDecorator;
import bottomtextdanny.braincell.libraries.registry.*;

public interface SegmentAdmin extends ContextualSerializable<Segment<?>> {

    default boolean addSegment(int op, SegmentTicket ticket) {
        Segment<?> segment = ticket.segment();
        if (segment.administrator == null) {
            segment.structurePos = ticket.position();
            if (onAddition(op, ticket)) {
                segment.administrator = this;
                segment.op = op;
            } else {
                segment.discarded = true;
                segment.structurePos = null;
                return false;
            }
        }
        return true;
    }

    default boolean onAddition(int op, SegmentTicket ticket) {
        return true;
    }

    default boolean onChildAddition(int op, SegmentTicket ticket) {
        return true;
    }

    default boolean onPreSolving(int op, SegmentTicket ticket) {
        return true;
    }

    default boolean onSolving(int op, SegmentTicket ticket) {
        return true;
    }

    default boolean doesPreSolving() {
        return false;
    }

    default boolean onIteratorAddition(SegmentDecorator decorator) {
        return true;
    }

    default boolean onLocalIteratorAddition(SegmentDecorator decorator) {
        return true;
    }

    default boolean onRecursiveIteratorAddition(SegmentDecorator decorator) {
        return true;
    }

    @Override
    Wrap<? extends ContextualSerializer<? extends SegmentAdmin, Segment<?>>> serializer();
}
