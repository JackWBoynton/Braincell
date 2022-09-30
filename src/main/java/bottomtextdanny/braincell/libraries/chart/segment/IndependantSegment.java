package bottomtextdanny.braincell.libraries.chart.segment;

import bottomtextdanny.braincell.libraries.chart.segment_maker.SegmentDecorator;
import bottomtextdanny.braincell.libraries.registry.ContextualSerializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import bottomtextdanny.braincell.tables.BCContextualSerializers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.Nullable;

public class IndependantSegment extends Segment<SegmentData> {

    public IndependantSegment() {
    }

    public IndependantSegment(CompoundTag root, @Nullable SegmentAdmin admin) {
        super(root, admin);
    }

    @Override
    public void construct(WorldGenLevel level, SegmentDecorator iterator, BlockPos pos, Rotation rotation) {
        MutableSegmentData data = new MutableSegmentData(level, null, null, null, null, NT_RANDOM);
        BlockState blockState = level.getBlockState(pos);
        data.setOriginalState(blockState);
        data.setBlockPos(pos);
        data.setLocalPos(BlockPos.ZERO);
        iterator.accept(data);
    }

    @Override
    public BoundingBox getDefaultLocalBoundingBox() {
        return ZERO;
    }

    @Override
    public Wrap<? extends ContextualSerializer<?, SegmentAdmin>> serializer() {
        return BCContextualSerializers.INDEPENDENT_SEGMENT;
    }
}
