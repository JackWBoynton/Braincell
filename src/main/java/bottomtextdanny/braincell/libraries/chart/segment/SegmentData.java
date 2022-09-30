package bottomtextdanny.braincell.libraries.chart.segment;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public interface SegmentData {

    WorldGenLevel level();

    void setState(BlockState state);

    BlockState state();

    BlockState originalState();

    BlockPos blockPos();

    BlockPos localPos();

    RandomSource random();


}
