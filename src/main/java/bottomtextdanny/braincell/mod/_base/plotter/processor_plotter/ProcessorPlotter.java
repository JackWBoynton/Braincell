package bottomtextdanny.braincell.mod._base.plotter.processor_plotter;

import bottomtextdanny.braincell.base.function.IntTriConsumer;
import bottomtextdanny.braincell.mod._base.plotter.PlotterRotator;
import bottomtextdanny.braincell.mod._base.plotter.SpecialPlotter;
import bottomtextdanny.braincell.mod._base.plotter.iterator.MutablePlotterData;
import bottomtextdanny.braincell.mod._base.plotter.iterator.PlotterIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public class ProcessorPlotter<DATA extends MutablePlotterData> extends SpecialPlotter<DATA> {
    private final ProcessorPlotter.Handler<DATA> handler;

    public ProcessorPlotter(LevelAccessor level, ProcessorPlotter.Handler<DATA> handler) {
        super(level);
        this.handler = handler;
    }

    @Override
    public void makeOwnThing(BlockPos pos, PlotterIterator<DATA> iterator, Rotation rotation) {
        DATA data = this.handler.preprocessor.apply(rotation, pos);
        BlockPos.MutableBlockPos localPos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos worldPos = new BlockPos.MutableBlockPos();

        if (this.handler.processor != null) {
            this.handler.processor.accept(pos, data, (x, y, z) -> {
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
                if (data.state() != data.originalState()) {
                    this.level.setBlock(worldPos, PlotterRotator.rotateBlockState(rotation, data.state()), 3);
                }
            });
        }
    }

    @FunctionalInterface
    public interface Processor<DATA> {
        void accept(BlockPos pos, DATA data, IntTriConsumer consumer);
    }

    public record Handler<DATA>(BiFunction<Rotation, BlockPos, DATA> preprocessor, Processor<DATA> processor) {}
}
