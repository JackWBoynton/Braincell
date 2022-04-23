package net.bottomtextdanny.braincell.mod._mod.object_tables;

import net.bottomtextdanny.braincell.Braincell;
import net.bottomtextdanny.braincell.mod._base.registry.managing.BCRegistry;
import net.bottomtextdanny.braincell.mod._base.registry.managing.RegistryHelper;
import net.bottomtextdanny.braincell.mod._base.registry.managing.Wrap;
import net.bottomtextdanny.braincell.mod.world.ChestOverriderRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;

public class BraincellRecipes {
    public static final BCRegistry<RecipeSerializer<?>> ENTRIES = new BCRegistry<>(false);
    public static final RegistryHelper<RecipeSerializer<?>> HELPER = new RegistryHelper<>(Braincell.DEFERRING_STATE, ENTRIES);

    public static Wrap<SimpleRecipeSerializer<ChestOverriderRecipe>> CHEST_OVERRIDER = HELPER.defer("chest_overrider", () -> new SimpleRecipeSerializer<>(ChestOverriderRecipe::new));
}
