package bottomtextdanny.braincell.mod._mod.common_sided;

import bottomtextdanny.braincell.Braincell;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public final class ChestOverriderManager {
   private final Map materialMap = Maps.newHashMap();

   public void put(ResourceLocation key, Supplier chestItemStack) {
      if (!Braincell.common().hasPassedInitialization()) {
         this.materialMap.put(key, chestItemStack);
      }

   }

   public boolean containsForId(ResourceLocation key) {
      return this.materialMap.containsKey(key);
   }

   public ItemStack getItemStack(ResourceLocation key) {
      return (ItemStack)((Supplier)this.materialMap.get(key)).get();
   }
}
