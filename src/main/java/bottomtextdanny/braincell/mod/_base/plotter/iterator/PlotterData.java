package bottomtextdanny.braincell.mod._base.plotter.iterator;

import bottomtextdanny.braincell.mod._base.plotter.Plotter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.random.RandomGenerator;

public interface PlotterData {
    Plotter<?> plotter();

    BlockState state();

    void setState(BlockState state);

    BlockState originalState();

    BlockPos blockPos();

    BlockPos localPos();

    RandomGenerator random();
}
