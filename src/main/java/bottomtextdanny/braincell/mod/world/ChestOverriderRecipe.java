package bottomtextdanny.braincell.mod.world;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.mod._mod.common_sided.ChestOverriderManager;
import bottomtextdanny.braincell.mod._mod.object_tables.BraincellRecipes;
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

   public boolean matches(CraftingContainer inv, Level worldIn) {
      ChestOverriderManager overrider = Braincell.common().getChestOverriderManager();
      Item currentItem = inv.getItem(0).getItem();
      if (!overrider.containsForId(currentItem.getRegistryName())) {
         return false;
      } else {
         for(int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack iItemStack = inv.getItem(i);
            Item iItem = iItemStack.getItem();
            if ((i != 4 || !iItemStack.isEmpty()) && (i == 4 || iItem != currentItem)) {
               return false;
            }
         }

         return true;
      }
   }

   public ItemStack assemble(CraftingContainer inv) {
      ChestOverriderManager overrider = Braincell.common().getChestOverriderManager();
      Item item = inv.getItem(0).getItem();
      return overrider.containsForId(item.getRegistryName()) ? overrider.getItemStack(item.getRegistryName()) : new ItemStack(Blocks.END_STONE);
   }

   public NonNullList getRemainingItems(CraftingContainer inv) {
      NonNullList nonnulllist = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);

      for(int i = 0; i < nonnulllist.size(); ++i) {
         ItemStack item = inv.getItem(i);
         if (item.hasContainerItem()) {
            nonnulllist.set(i, item.getContainerItem());
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

   public RecipeSerializer getSerializer() {
      return (RecipeSerializer)BraincellRecipes.CHEST_OVERRIDER.get();
   }
}
