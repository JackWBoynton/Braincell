package bottomtextdanny.braincell.libraries.chart.segment;

import bottomtextdanny.braincell.libraries.chart.segment_maker.SegmentPregeneration;
import bottomtextdanny.braincell.libraries.chart.segment_maker.SegmentPregenerations;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;

public sealed interface SegmentTicket {

    static SegmentTicket rigid(BlockPos offset, Segment<?> segment) {
        return new Impl(offset, SegmentPregenerations.spgPass(), Rotation.NONE, segment);
    }

    static SegmentTicket rigid(BlockPos offset, SegmentPregeneration modifier, Segment<?> segment) {
        return new Impl(offset, modifier, Rotation.NONE, segment);
    }

    static SegmentTicket orientable(BlockPos offset, Rotation rotation, Segment<?> segment) {
        return new Impl(offset, SegmentPregenerations.spgPass(), rotation, segment);
    }

    static SegmentTicket orientable(BlockPos offset, SegmentPregeneration modifier, Rotation rotation, Segment<?> segment) {
        return new Impl(offset, modifier, rotation, segment);
    }

    static SegmentTicket identity(Segment<?> segment) {
        return new Identity(segment);
    }

    static SegmentTicket offset(SegmentTicket base, SegmentTicket additive) {
        return new Impl(base.position().offset(ChartRotator.rotatedOf(base.rotation(), additive.position())), additive.modifier(), base.rotation().getRotated(additive.rotation()), additive.segment());
    }

    BlockPos position();

    SegmentPregeneration modifier();

    Rotation rotation();

    Segment<?> segment();

    record Identity(Segment<?> segment) implements SegmentTicket {

        @Override
        public BlockPos position() {
            return BlockPos.ZERO;
        }

        @Override
        public SegmentPregeneration modifier() {
            return SegmentPregenerations.spgPass();
        }

        @Override
        public Rotation rotation() {
            return Rotation.NONE;
        }
    }

    record Impl(BlockPos position, SegmentPregeneration modifier, Rotation rotation, Segment<?> segment) implements SegmentTicket {}
}
