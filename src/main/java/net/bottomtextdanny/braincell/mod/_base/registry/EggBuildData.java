package net.bottomtextdanny.braincell.mod._base.registry;

import net.bottomtextdanny.braincell.mod.world.builtin_items.BCSpawnEggItem;
import net.minecraft.resources.ResourceLocation;

public record EggBuildData(String name, BCSpawnEggItem.Builder builder) {
}
