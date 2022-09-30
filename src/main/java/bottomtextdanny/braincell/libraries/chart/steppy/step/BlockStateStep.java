package bottomtextdanny.braincell.libraries.chart.steppy.step;

import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.libraries.chart.steppy.data.Data;
import bottomtextdanny.braincell.libraries.chart.steppy.iteration.TransientData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockStateStep extends Step {

    BlockState invokeBlockState(Data data, ObjectFetcher metadata, TransientData preceding);

    @Override
    default BlockState invoke(Data data, ObjectFetcher metadata, TransientData preceding) {
        return invokeBlockState(data, metadata, preceding);
    }
}
