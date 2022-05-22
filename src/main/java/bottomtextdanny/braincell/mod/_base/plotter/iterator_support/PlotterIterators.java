package bottomtextdanny.braincell.mod._base.plotter.iterator_support;

import bottomtextdanny.braincell.mod._base.plotter.Plotter;
import bottomtextdanny.braincell.mod._base.plotter.iterator.PlotterData;
import bottomtextdanny.braincell.mod._base.plotter.iterator.PlotterIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;
import java.util.function.Predicate;

public final class PlotterIterators {

    public static <T extends PlotterData> IfElsePlotterIterator<T> ifBlockState(Predicate<BlockState> block, PlotterIterator<T> happen) {
        return new IfElsePlotterIterator<T>((d) -> block.test(d.state()), happen);
    }

    public static <T extends PlotterData> IfElsePlotterIterator<T> randomly(float possibility, PlotterIterator<T> happen) {
        return new IfElsePlotterIterator<T>((d) -> d.random().nextFloat() < possibility, happen);
    }

    public static <T extends PlotterData> PlotterIterator<T> place(BlockState blockState) {
        return (d) -> {
            d.setState(blockState);
        };
    }

    public static <T extends PlotterData> PlotterIterator<T> decorate(Plotter<?> deco) {
        return (d) -> {
            d.plotter().addChild(d.localPos(), deco);
        };
    }

    public static <T extends PlotterData> PlotterIterator<T> decorateOffset(Plotter<?> deco, Function<BlockPos, BlockPos> posTransformer) {
        return (d) -> {
            d.plotter().addChild(posTransformer.apply(d.localPos()), deco);
        };
    }
}
