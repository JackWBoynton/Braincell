package bottomtextdanny.braincell.mod.entity.psyche.extras;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

public final class BCAABBs {
   public static AABB livingEntityRange(LivingEntity entity, double horizontalRange, double verticalRange) {
      return new AABB(entity.position().subtract(horizontalRange, verticalRange, horizontalRange), entity.position().add(horizontalRange, verticalRange, horizontalRange));
   }

   public static AABB livingEntityRange(LivingEntity entity, double horizontalRange, double floorOffset, double topOffset) {
      return new AABB(entity.position().subtract(horizontalRange, floorOffset, horizontalRange), entity.position().add(horizontalRange, topOffset, horizontalRange));
   }

   private BCAABBs() {
   }
}
