package bottomtextdanny.braincell.libraries.chart.steppy.data;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockStateSetterArgument {

    void setBlockState(BlockPos pos, BlockState newBlockState, int flag);
}
