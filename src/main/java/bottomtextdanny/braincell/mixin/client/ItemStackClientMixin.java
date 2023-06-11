package bottomtextdanny.braincell.mixin.client;

import bottomtextdanny.braincell.mixin_support.ItemStackClientExtensor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ItemStack.class})
public class ItemStackClientMixin implements ItemStackClientExtensor {
   public LivingEntity cachedHolder;

   public void setCachedHolder(LivingEntity entity) {
      this.cachedHolder = entity;
   }

   public LivingEntity getCachedHolder() {
      return this.cachedHolder;
   }
}
