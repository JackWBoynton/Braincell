package net.bottomtextdanny.braincell.mod._base.plotter;

import net.bottomtextdanny.braincell.mod._base.plotter.iterator.MutablePlotterData;
import net.bottomtextdanny.braincell.mod._base.plotter.iterator.PlotterData;
import net.bottomtextdanny.braincell.mod._base.plotter.iterator.PlotterIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.random.RandomGenerator;

public class PlanePlotter extends SpecialPlotter<PlanePlotter.Data> {
    protected final IntPlane plane;

    public PlanePlotter(LevelAccessor level, IntPlane plane) {
        super(level);
        this.plane = plane;
    }

    public IntPlane getPlane() {
        return this.plane;
    }

    @Override
    public void makeOwnThing(BlockPos pos, PlotterIterator<Data> iterator, Rotation rotation) {
        PlanePlotter.MuData data = new PlanePlotter.MuData(this, this.plane, null, null, null, null, NT_RANDOM);
        BlockPos.MutableBlockPos worldPos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos localPos = new BlockPos.MutableBlockPos();
        if (rotation != Rotation.NONE) {
            this.plane.iteratePlane((xl, yl) -> {
                localPos.set(IntPlane.FIXER_BY_AXIS.get(this.plane.getAxis().ordinal()).apply(xl, yl));
                PlotterRotator.rotate(rotation, localPos);
                worldPos.set(pos.getX() + localPos.getX(), pos.getY() + localPos.getY(), pos.getZ() + localPos.getZ());
                BlockState state = this.level.getBlockState(worldPos);
                data.setProg(xl - this.plane.x1, yl - this.plane.y1);
                data.setState(state);
                data.setOriginalState(state);
                data.setLocalPos(localPos);
                data.setBlockPos(worldPos);
                iterator.accept(data);
                this.level.setBlock(worldPos, PlotterRotator.rotateBlockState(rotation, data.state()), 3);
            });
        } else {
            this.plane.iteratePlane((xl, yl) -> {
                localPos.set(IntPlane.FIXER_BY_AXIS.get(this.plane.getAxis().ordinal()).apply(xl, yl));
                worldPos.set(pos.getX() + localPos.getX(), pos.getY() + localPos.getY(), pos.getZ() + localPos.getZ());
                BlockState state = this.level.getBlockState(worldPos);
                data.setProg(xl - this.plane.x1, yl - this.plane.y1);
                data.setState(state);
                data.setOriginalState(state);
                data.setLocalPos(localPos);
                data.setBlockPos(worldPos);
                iterator.accept(data);
                this.level.setBlock(worldPos, data.state(), 3);
            });
        }
    }

    public interface Data extends PlotterData {
        IntPlane plane();
        int xProg();
        int yProg();
    }

    private static class MuData extends MutablePlotterData implements Data {
        private final IntPlane plane;
        private int xProg;
        private int yProg;

        public MuData(Plotter<?> plotter, IntPlane plane, BlockState state, BlockState originalState, BlockPos blockPos, BlockPos localPos, RandomGenerator random) {
            super(plotter, state, originalState, blockPos, localPos, random);
            this.plane = plane;
        }

        public void setProg(int x, int y) {
            this.xProg = x;
            this.yProg = y;
        }

        @Override
        public IntPlane plane() {
            return this.plane;
        }

        @Override
        public int xProg() {
            return this.xProg;
        }

        @Override
        public int yProg() {
            return this.yProg;
        }
    }
}
