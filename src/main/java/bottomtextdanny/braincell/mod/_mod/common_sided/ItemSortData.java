package bottomtextdanny.braincell.mod._mod.common_sided;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;

public final class ItemSortData implements AutoCloseable {
   private final Map sortValueMap = Maps.newHashMapWithExpectedSize(256);

   public void setSortValue(ResourceLocation key, short sortValue) {
      this.sortValueMap.put(key, sortValue);
   }

   public short getSortValue(ResourceLocation key) {
      return !this.sortValueMap.containsKey(key) ? -1 : (Short)this.sortValueMap.get(key);
   }

   public void close() {
      this.sortValueMap.clear();
   }
}
