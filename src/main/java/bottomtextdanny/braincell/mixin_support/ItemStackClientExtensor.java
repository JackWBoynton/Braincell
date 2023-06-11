package bottomtextdanny.braincell.mixin_support;

import net.minecraft.world.entity.LivingEntity;

public interface ItemStackClientExtensor {
   void setCachedHolder(LivingEntity var1);

   LivingEntity getCachedHolder();
}
