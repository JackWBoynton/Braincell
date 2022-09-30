/*
 * Copyright Monday September 12 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.tables;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.registry.BCRegistry;
import bottomtextdanny.braincell.libraries.registry.RegistryHelper;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class BCItems {
	public static final BCRegistry<Item> ENTRIES = new BCRegistry<>();
	public static final RegistryHelper<Item> HELPER = new RegistryHelper<>(Braincell.DEFERRING_STATE, ENTRIES);

	public static final Wrap<BlockItem> FLAGGED_SCHEMA_BLOCK = HELPER.defer("flagged_schema_block", () -> new BlockItem(BCBlocks.FLAGGED_SCHEMA_BLOCK.get(), new Item.Properties()));

}
