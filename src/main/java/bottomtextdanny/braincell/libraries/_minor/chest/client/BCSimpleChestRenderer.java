package bottomtextdanny.braincell.libraries._minor.chest.client;

import bottomtextdanny.braincell.libraries._minor.chest.BCChestBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.LidBlockEntity;

public class BCSimpleChestRenderer<T extends BCChestBlockEntity & LidBlockEntity> extends BCBaseChestRenderer<T> {

    public BCSimpleChestRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(rendererDispatcherIn, new BCBaseSingleChestModel(), new BCBaseLeftChestModel(), new BCBaseRightChestModel());
    }
}
