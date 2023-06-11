package bottomtextdanny.braincell.base;

import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public final class BCRotVector {
   public static YP_DoubleLookStart start(double x, double y, double z) {
      return new YP_DoubleLookStart(x, y, z);
   }

   public static YP_DoubleLookStart start(Position vector) {
      return new YP_DoubleLookStart((double)((float)vector.x()), (double)((float)vector.y()), (double)((float)vector.z()));
   }

   public static YP_DoubleLookStart start(Entity vector) {
      return new YP_DoubleLookStart((double)((float)vector.position().x()), (double)((float)vector.position().y()), (double)((float)vector.position().z()));
   }

   public static YP_DoubleLookStart start(Vec3i integerPosition) {
      return new YP_DoubleLookStart((double)((float)integerPosition.getX() + 0.5F), (double)((float)integerPosition.getY() + 0.5F), (double)((float)integerPosition.getZ() + 0.5F));
   }

   public static record YP_DoubleLookStart(double x, double y, double z) {
      public YP_DoubleLookStart(double x, double y, double z) {
         this.x = x;
         this.y = y;
         this.z = z;
      }

      public Vec3 get(double endX, double endY, double endZ) {
         double xDif = this.x - endX;
         double yDif = this.y - endY;
         double zDif = this.z - endZ;
         double squareDif = (double)Mth.sqrt((float)(xDif * xDif + zDif * zDif));
         double yaw = Mth.atan2(yDif, xDif) * 0.017453292519943295 - 90.0;
         double pitch = -(Mth.atan2(yDif, squareDif) * 0.017453292519943295);
         float yCos = BCMath.cos(-yaw * 0.017453292519943295 - Math.PI);
         float ySin = BCMath.sin(-yaw * 0.017453292519943295 - Math.PI);
         float pCos = -BCMath.cos(-pitch * 0.017453292519943295);
         float pSin = BCMath.sin(-pitch * 0.017453292519943295);
         return new Vec3((double)(ySin * pCos), (double)pSin, (double)(yCos * pCos));
      }

      public Vec3 get(Position vector) {
         return this.get(vector.x(), vector.y(), vector.z());
      }

      public Vec3 get(Entity entity) {
         return this.get(entity.position().x, entity.position().y, entity.position().z);
      }

      public Vec3 get(Vec3i integerPosition) {
         return this.get((double)((float)integerPosition.getX() + 0.5F), (double)((float)integerPosition.getY() + 0.5F), (double)((float)integerPosition.getZ() + 0.5F));
      }

      public double x() {
         return this.x;
      }

      public double y() {
         return this.y;
      }

      public double z() {
         return this.z;
      }
   }
}
