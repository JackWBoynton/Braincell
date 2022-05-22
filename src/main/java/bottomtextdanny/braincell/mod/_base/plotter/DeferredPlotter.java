package bottomtextdanny.braincell.mod._base.plotter;

import bottomtextdanny.braincell.base.function.TriFunction;
import bottomtextdanny.braincell.mod._base.plotter.iterator.MutablePlotterData;
import bottomtextdanny.braincell.mod._base.plotter.iterator.PlotterData;
import bottomtextdanny.braincell.mod._base.plotter.iterator.PlotterIterator;
import bottomtextdanny.braincell.base.function.IntTriConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.util.TriConsumer;

public class DeferredPlotter<T> extends SpecialPlotter<PlotterData> {
    private T cachedIterationData;
    private final CacheHandler<T> cacheHandler;

    public DeferredPlotter(LevelAccessor level, CacheHandler<T> cacheHandler) {
        super(level);
        this.cacheHandler = cacheHandler;
    }

    @Override
    public void makeOwnThing(BlockPos pos, PlotterIterator<PlotterData> iterator, Rotation rotation) {
        MutablePlotterData data = new MutablePlotterData(this, null, null, null, null, NT_RANDOM);
        BlockPos.MutableBlockPos worldPos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos localPos = new BlockPos.MutableBlockPos();

        if (this.cachedIterationData == null) {
            this.cachedIterationData = this.cacheHandler.cacheGetter.apply(rotation, pos, this.level);
        }

        if (rotation != Rotation.NONE) {
            this.cacheHandler.optimizedIterator.accept(rotation, (x, y, z) -> {
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
            }, this.cachedIterationData);
        } else {
            this.cacheHandler.optimizedIterator.accept(rotation, (x, y, z) -> {
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
                this.level.setBlock(worldPos, data.state(), 3);
            }, this.cachedIterationData);
        }
    }

    public T getCache() {
        return this.cachedIterationData;
    }

    record CacheHandler<T>(TriFunction<Rotation, BlockPos, LevelAccessor, T> cacheGetter,
                           TriConsumer<Rotation, IntTriConsumer, T> optimizedIterator) {}
}
