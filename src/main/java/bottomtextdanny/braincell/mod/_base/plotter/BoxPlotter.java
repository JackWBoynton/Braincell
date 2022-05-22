package bottomtextdanny.braincell.mod._base.plotter;

import bottomtextdanny.braincell.mod._base.plotter.iterator.MutablePlotterData;
import bottomtextdanny.braincell.mod._base.plotter.iterator.PlotterData;
import bottomtextdanny.braincell.mod._base.plotter.iterator.PlotterIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.random.RandomGenerator;

public class BoxPlotter extends SpecialPlotter<BoxPlotter.Data> {
    protected final IntBox box;

    public BoxPlotter(LevelAccessor level, IntBox box) {
        super(level);
        this.box = box;
    }

    public IntBox getBox() {
        return this.box;
    }

    @Override
    public void makeOwnThing(BlockPos pos, PlotterIterator<Data> iterator, Rotation rotation) {
        BoxPlotter.MuData data = new BoxPlotter.MuData(this, this.box, null, null, null, null, NT_RANDOM);
        BlockPos.MutableBlockPos worldPos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos localPos = new BlockPos.MutableBlockPos();

        if (rotation != Rotation.NONE) {
            this.box.iterate((x, y, z) -> {
                localPos.set(x, y, z);

                PlotterRotator.rotate(rotation, localPos);
                worldPos.set(pos.getX() + localPos.getX(),
                        pos.getY() + localPos.getY(),
                        pos.getZ() + localPos.getZ());
                BlockState state = this.level.getBlockState(worldPos);
                data.setProg(x - this.box.x1, y - this.box.y1, z - this.box.z1);
                data.setState(state);
                data.setOriginalState(state);
                data.setLocalPos(localPos);
                data.setBlockPos(worldPos);
                iterator.accept(data);
                this.level.setBlock(worldPos, PlotterRotator.rotateBlockState(rotation, data.state()), 3);
            });
        } else {
            this.box.iterate((x, y, z) -> {
                localPos.set(x, y, z);
                worldPos.set(pos.getX() + localPos.getX(),
                        pos.getY() + localPos.getY(),
                        pos.getZ() + localPos.getZ());
                BlockState state = this.level.getBlockState(worldPos);
                data.setProg(x - this.box.x1, y - this.box.y1, z - this.box.z1);
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
        IntBox box();
        int xProg();
        int yProg();
        int zProg();
    }

    private static class MuData extends MutablePlotterData implements Data {
        private final IntBox box;
        private int xProg;
        private int yProg;
        private int zProg;

        public MuData(Plotter<?> plotter, IntBox box, BlockState state, BlockState originalState, BlockPos blockPos, BlockPos localPos, RandomGenerator random) {
            super(plotter, state, originalState, blockPos, localPos, random);
            this.box = box;
        }

        public void setProg(int x, int y, int z) {
            this.xProg = x;
            this.yProg = y;
            this.zProg = z;
        }

        @Override
        public IntBox box() {
            return this.box;
        }

        @Override
        public int xProg() {
            return this.xProg;
        }

        @Override
        public int yProg() {
            return this.yProg;
        }

        @Override
        public int zProg() {
            return this.zProg;
        }
    }
}
