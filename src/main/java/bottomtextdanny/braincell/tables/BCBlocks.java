/*
 * Copyright Monday September 12 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.tables;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.flagged_schema_block.FlaggedSchemaBlock;
import bottomtextdanny.braincell.libraries.registry.BCRegistry;
import bottomtextdanny.braincell.libraries.registry.RegistryHelper;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public final class BCBlocks {
	public static final BCRegistry<Block> ENTRIES = new BCRegistry<>();
	public static final RegistryHelper<Block> HELPER = new RegistryHelper<>(Braincell.DEFERRING_STATE, ENTRIES);

	public static final Wrap<FlaggedSchemaBlock> FLAGGED_SCHEMA_BLOCK = HELPER.defer("flagged_schema_block", () -> new FlaggedSchemaBlock(BlockBehaviour.Properties.of(Material.GRASS, MaterialColor.COLOR_YELLOW).strength(0.5F).sound(SoundType.GRASS)));
}
