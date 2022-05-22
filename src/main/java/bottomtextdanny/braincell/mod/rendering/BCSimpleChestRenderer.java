package bottomtextdanny.braincell.mod.rendering;

import bottomtextdanny.braincell.mod.rendering.modeling.chest.BCBaseLeftChestModel;
import bottomtextdanny.braincell.mod.rendering.modeling.chest.BCBaseRightChestModel;
import bottomtextdanny.braincell.mod.rendering.modeling.chest.BCBaseSingleChestModel;
import bottomtextdanny.braincell.mod.world.builtin_block_entities.BCChestBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.LidBlockEntity;

public class BCSimpleChestRenderer<T extends BCChestBlockEntity & LidBlockEntity> extends BCBaseChestRenderer<T> {

    public BCSimpleChestRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(rendererDispatcherIn, new BCBaseSingleChestModel(), new BCBaseLeftChestModel(), new BCBaseRightChestModel());
    }
}
