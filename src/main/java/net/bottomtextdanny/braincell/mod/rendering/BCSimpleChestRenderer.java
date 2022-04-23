package net.bottomtextdanny.braincell.mod.rendering;

import net.bottomtextdanny.braincell.mod.rendering.modeling.chest.BCBaseLeftChestModel;
import net.bottomtextdanny.braincell.mod.rendering.modeling.chest.BCBaseRightChestModel;
import net.bottomtextdanny.braincell.mod.rendering.modeling.chest.BCBaseSingleChestModel;
import net.bottomtextdanny.braincell.mod.world.builtin_block_entities.BCChestBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.LidBlockEntity;

public class BCSimpleChestRenderer<T extends BCChestBlockEntity & LidBlockEntity> extends BCBaseChestRenderer<T> {

    public BCSimpleChestRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(rendererDispatcherIn, new BCBaseSingleChestModel(), new BCBaseLeftChestModel(), new BCBaseRightChestModel());
    }
}
