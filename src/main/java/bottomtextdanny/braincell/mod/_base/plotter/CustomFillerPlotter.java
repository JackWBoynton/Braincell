package bottomtextdanny.braincell.mod._base.plotter;

import bottomtextdanny.braincell.mod._base.plotter.iterator.MutablePlotterData;
import bottomtextdanny.braincell.mod._base.plotter.iterator.PlotterData;
import bottomtextdanny.braincell.mod._base.plotter.iterator.PlotterIterator;
import bottomtextdanny.braincell.base.function.IntTriConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

public class CustomFillerPlotter extends SpecialPlotter<PlotterData> {
    private final Consumer<IntTriConsumer> iterator;

    public CustomFillerPlotter(LevelAccessor level, Consumer<IntTriConsumer> iteratorFunction) {
        super(level);
        this.iterator = iteratorFunction;
    }

    @Override
    public void makeOwnThing(BlockPos pos, PlotterIterator<PlotterData> iterator, Rotation rotation) {
        MutablePlotterData data = new MutablePlotterData(this, null, null, null, null, NT_RANDOM);
        BlockPos.MutableBlockPos worldPos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos localPos = new BlockPos.MutableBlockPos();

        if (rotation != Rotation.NONE) {
            this.iterator.accept((x, y, z) -> {
                localPos.set(x, y, z);
                PlotterRotator.rotate(rotation, localPos);
                worldPos.set(pos.getX() + localPos.getX(),
                        pos.getY() + localPos.getY(),
                        pos.getZ() + localPos.getZ());
                BlockState state = this.level.getBlockState(worldPos);
                data.setState(state);
                data.setOriginalState(state);
                data.setLocalPos(localPos);
                data.setBlockPos(worldPos);
                iterator.accept(data);
                this.level.setBlock(worldPos, PlotterRotator.rotateBlockState(rotation, data.state()), 3);
            });
        } else {
            this.iterator.accept((x, y, z) -> {
                localPos.set(x, y, z);
                worldPos.set(pos.getX() + localPos.getX(),
                        pos.getY() + localPos.getY(),
                        pos.getZ() + localPos.getZ());
                BlockState state = this.level.getBlockState(worldPos);
                data.setState(state);
                data.setOriginalState(state);
                data.setLocalPos(localPos);
                data.setBlockPos(worldPos);
                iterator.accept(data);
                this.level.setBlock(worldPos, data.state(), 3);
            });
        }
    }
}
