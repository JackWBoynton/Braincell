package bottomtextdanny.braincell.libraries.chart.segment;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public class MutableSegmentData implements SegmentData {
    private final WorldGenLevel level;
    private BlockState state;
    private BlockState originalState;
    private BlockPos blockPos;
    private BlockPos localPos;
    private final RandomSource random;

    public MutableSegmentData(WorldGenLevel level, BlockState state, BlockState originalState,
                              BlockPos blockPos, BlockPos localPos,
                              RandomSource random) {
        this.level = level;
        this.state = state;
        this.originalState = originalState;
        this.blockPos = blockPos;
        this.localPos = localPos;
        this.random = random;
    }

    @Override
    public WorldGenLevel level() {
        return level;
    }

    @Override
    public BlockState state() {
        return state;
    }

    @Override
    public void setState(BlockState state) {
        this.state = state;
    }

    @Override
    public BlockState originalState() {
        return originalState;
    }

    public void setOriginalState(BlockState originalState) {
        this.originalState = originalState;
    }

    @Override
    public BlockPos blockPos() {
        return blockPos;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    @Override
    public BlockPos localPos() {
        return localPos;
    }

    public void setLocalPos(BlockPos localPos) {
        this.localPos = localPos;
    }

    @Override
    public RandomSource random() {
        return random;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (MutableSegmentData) obj;
        return Objects.equals(this.random, that.random);
    }

    @Override
    public int hashCode() {
        return Objects.hash(random);
    }

    @Override
    public String toString() {
        return "Data[" +
                "random=" + random + ']';
    }
}
