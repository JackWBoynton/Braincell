package bottomtextdanny.braincell.mod.rendering;

import bottomtextdanny.braincell.mod.rendering.modeling.chest.BCBaseLeftChestModel;
import bottomtextdanny.braincell.mod.rendering.modeling.chest.BCBaseRightChestModel;
import bottomtextdanny.braincell.mod.rendering.modeling.chest.BCBaseSingleChestModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class BCSimpleChestRenderer extends BCBaseChestRenderer {
   public BCSimpleChestRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
      super(rendererDispatcherIn, new BCBaseSingleChestModel(), new BCBaseLeftChestModel(), new BCBaseRightChestModel());
   }
}
