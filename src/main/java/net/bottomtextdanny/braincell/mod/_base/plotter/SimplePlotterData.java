package net.bottomtextdanny.braincell.mod._base.plotter;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;
import java.util.random.RandomGenerator;

public final class SimplePlotterData {
    private final Plotter<?> plotter;
    private BlockState state;
    private final BlockState originalState;
    private final BlockPos blockPos;
    private final BlockPos localPos;
    private final RandomGenerator random;

    public SimplePlotterData(Plotter<?> plotter,
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

    public Plotter<?> plotter() {
        return plotter;
    }

    public BlockState state() {
        return state;
    }

    public void setState(BlockState state) {
        this.state = state;
    }

    public BlockState originalState() {
        return originalState;
    }

    public BlockPos blockPos() {
        return blockPos;
    }

    public BlockPos localPos() {
        return localPos;
    }

    public RandomGenerator random() {
        return random;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SimplePlotterData) obj;
        return Objects.equals(this.plotter, that.plotter) &&
                Objects.equals(this.blockPos, that.blockPos) &&
                Objects.equals(this.localPos, that.localPos) &&
                Objects.equals(this.random, that.random);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plotter, blockPos, localPos, random);
    }

    @Override
    public String toString() {
        return "Data[" +
                "plotter=" + plotter + ", " +
                "blockPos=" + blockPos + ", " +
                "localPos=" + localPos + ", " +
                "random=" + random + ']';
    }
}
