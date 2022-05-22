package bottomtextdanny.braincell.mod._base.plotter.iterator;

import bottomtextdanny.braincell.mod._base.plotter.Plotter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;
import java.util.random.RandomGenerator;

public class MutablePlotterData implements PlotterData {
    private final Plotter<?> plotter;
    private BlockState state;
    private BlockState originalState;
    private BlockPos blockPos;
    private BlockPos localPos;
    private final RandomGenerator random;

    public MutablePlotterData(Plotter<?> plotter,
                              BlockState state, BlockState originalState,
                              BlockPos blockPos, BlockPos localPos,
                              RandomGenerator random) {
        this.plotter = plotter;
        this.state = state;
        this.originalState = originalState;
        this.blockPos = blockPos;
        this.localPos = localPos;
        this.random = random;
    }

    @Override
    public Plotter<?> plotter() {
        return plotter;
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
    public RandomGenerator random() {
        return random;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (MutablePlotterData) obj;
        return Objects.equals(this.plotter, that.plotter) &&
                Objects.equals(this.random, that.random);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plotter, random);
    }

    @Override
    public String toString() {
        return "Data[" +
                "plotter=" + plotter + ", " +
                "random=" + random + ']';
    }
}
