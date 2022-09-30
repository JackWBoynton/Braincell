/*
 * Copyright Monday September 12 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.tables;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries._minor.chest.client.BCSimpleChestRenderer;
import bottomtextdanny.braincell.libraries.flagged_schema_block.FlaggedSchemaBlockEntity;
import bottomtextdanny.braincell.libraries.flagged_schema_block.FlaggedSchemaBlockRenderer;
import bottomtextdanny.braincell.libraries.registry.BCRegistry;
import bottomtextdanny.braincell.libraries.registry.RegistryHelper;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public final class BCBlockEntities {
	public static final BCRegistry<BlockEntityType<?>> ENTRIES = new BCRegistry<>();
	public static final RegistryHelper<BlockEntityType<?>> HELPER = new RegistryHelper<>(Braincell.DEFERRING_STATE, ENTRIES);

	public static final Wrap<BlockEntityType<FlaggedSchemaBlockEntity>> FLAGGED_SCHEMA_BLOCK =
		HELPER.defer("flagged_schema_block", () -> BlockEntityType.Builder.of(
				FlaggedSchemaBlockEntity::new, BCBlocks.FLAGGED_SCHEMA_BLOCK.get())
			.build(null));

	@OnlyIn(Dist.CLIENT)
	public static void registerBlockEntityRenderers(FMLClientSetupEvent event) {
		BlockEntityRenderers.register(FLAGGED_SCHEMA_BLOCK.get(), FlaggedSchemaBlockRenderer::new);
	}
}
