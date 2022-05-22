package bottomtextdanny.braincell.mod._base.plotter;

import bottomtextdanny.braincell.base.function.IntTriFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.function.Predicate;

public final class DeferredPlotters {


    public static DeferredPlotter.CacheHandler<PillarCache> pillarUntilBlocked(Direction dir,
                                                                               Predicate<BlockPos> predicate,
                                                                               int stopAt,
                                                                               int off1s, int off2s,
                                                                               int off1e, int off2e) {
        return new DeferredPlotter.CacheHandler<>((rot, root, level) -> {
            Direction direction = rot.rotate(dir);
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
            Direction.Axis axis = direction.getAxis();
            IntTriFunction<BlockPos> axisPosGetter = PlotterExtras.AXIS_POS_GETTER.get(axis.ordinal());
            int[][] cacheMatrix = new int[off1e - off1s + 1][off2e - off2s + 1];
            int maxHeight = 0;
            for (int offset1 = off1s; offset1 <= off1e; offset1++) {
                for (int offset2 = off2s; offset2 <= off2e; offset2++) {
                    BlockPos rotated = PlotterRotator.rotatedOf(rot, new BlockPos(offset1, 0, offset2));
                    pos.set(root.offset(axisPosGetter.apply(0, rotated.getX(), rotated.getZ())));
                    while(predicate.test(pos) && cacheMatrix[offset1][offset2] <= stopAt) {

                        int height = cacheMatrix[offset1][offset2]++;
                        if (maxHeight < height) {
                            maxHeight = height;
                        }
                        pos.set(pos.move(direction));
                    }
                }
            }
            return new PillarCache(axis, cacheMatrix, maxHeight);
        }, (rotator, iterator, cache) -> {
            PlotterExtras.AxisPosSetter axisPosSetter = PlotterExtras.AXIS_POS_SETTER.get(cache.axis.ordinal());
            BlockPos.MutableBlockPos posHolder = new BlockPos.MutableBlockPos();
            int step = dir.getAxisDirection().getStep();
            for (int offset1 = off1s; offset1 <= off1e; offset1++) {
                for (int offset2 = off2s; offset2 <= off2e; offset2++) {
                    int length = (cache.lengthMatrix[offset1][offset2] - 1) * step;
                    int max = Math.max(length, 0);
                    int min = Math.min(length, 0);
                    for (int axisCoord = min; axisCoord <= max; axisCoord++) {
                        axisPosSetter.accept(posHolder, axisCoord, offset1, offset2);
                        iterator.accept(posHolder.getX(), posHolder.getY(), posHolder.getZ());
                    }
                }
            }
        });
    }

    public record PillarCache(Direction.Axis axis, int[][] lengthMatrix, int maxHeight) {}
}
