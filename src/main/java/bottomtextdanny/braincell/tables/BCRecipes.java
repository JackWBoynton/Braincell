package bottomtextdanny.braincell.tables;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.registry.BCRegistry;
import bottomtextdanny.braincell.libraries.registry.RegistryHelper;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import bottomtextdanny.braincell.libraries._minor.chest.ChestOverriderRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;

public final class BCRecipes {
    public static final BCRegistry<RecipeSerializer<?>> ENTRIES = new BCRegistry<>();
    public static final RegistryHelper<RecipeSerializer<?>> HELPER = new RegistryHelper<>(Braincell.DEFERRING_STATE, ENTRIES);

    public static Wrap<SimpleRecipeSerializer<ChestOverriderRecipe>> CHEST_OVERRIDER = HELPER.defer("chest_overrider", () -> new SimpleRecipeSerializer<>(ChestOverriderRecipe::new));
}
