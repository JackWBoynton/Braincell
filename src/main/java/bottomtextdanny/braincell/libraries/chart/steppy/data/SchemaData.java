package bottomtextdanny.braincell.libraries.chart.steppy.data;

import bottomtextdanny.braincell.libraries.chart.schema.FlagsEntry;
import net.minecraft.world.level.block.state.BlockState;

public interface SchemaData {

    BlockState injectSchemaProperties(BlockState blockState);

    FlagsEntry schemaFlags();
}
