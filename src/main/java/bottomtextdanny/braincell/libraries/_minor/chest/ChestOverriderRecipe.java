package bottomtextdanny.braincell.libraries._minor.chest;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.tables.BCRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class ChestOverriderRecipe extends CustomRecipe {

    public ChestOverriderRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
        ChestOverriderManager overrider = Braincell.common().getChestOverriderManager();
    	Item currentItem = inv.getItem(0).getItem();
        if (overrider.containsForId(currentItem.builtInRegistryHolder().key().location())) {
            for(int i = 0; i < inv.getContainerSize(); ++i) {
                ItemStack iItemStack = inv.getItem(i);
                Item iItem = iItemStack.getItem();
                if (!(i == 4 && iItemStack.isEmpty() || i != 4 && iItem == currentItem)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv) {
        ChestOverriderManager overrider = Braincell.common().getChestOverriderManager();
		Item item = inv.getItem(0).getItem();

        ResourceLocation id = item.builtInRegistryHolder().key().location();

        if (overrider.containsForId(id)) {
			return overrider.getItemStack(id);
		}

        return new ItemStack(Blocks.END_STONE);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);

        for(int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack item = inv.getItem(i);
            if (item.hasCraftingRemainingItem()) {
                nonnulllist.set(i, item.getCraftingRemainingItem());
            }

            if (item != ItemStack.EMPTY && item.getCount() > 0) {
                item.shrink(1);
            }
        }

        return nonnulllist;
    }

    public boolean canCraftInDimensions(int width, int height) {
        return width * height <= 9;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BCRecipes.CHEST_OVERRIDER.get();
    }
}
