package bottomtextdanny.braincell;

import bottomtextdanny.braincell.base.vector.BCDistance2Getter;
import bottomtextdanny.braincell.base.vector.BCDistance3Getter;
import bottomtextdanny.braincell.mixin_support.ItemStackClientExtensor;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public final class BraincellHelper {
   public static final UUID EMPTY_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
   public static final IllegalArgumentException INVALID_TOKEN_EX = new IllegalArgumentException("Tried to modify data with invalid token");
   public static final BCDistance3Getter RENDER_DIST3D_UTIL = new BCDistance3Getter();
   public static final BCDistance2Getter RENDER_DIST2D_UTIL = new BCDistance2Getter();
   public static final BCDistance3Getter LOGIC_DIST3D_UTIL = new BCDistance3Getter();
   public static final BCDistance2Getter LOGIC_DIST2D_UTIL = new BCDistance2Getter();

   public static String getCallerCallerClassName() {
      StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
      String callerClassName = null;

      for(int i = 1; i < stElements.length; ++i) {
         StackTraceElement ste = stElements[i];
         if (!ste.getClassName().equals(Braincell.class.getName()) && ste.getClassName().indexOf("java.lang.Thread") != 0) {
            if (callerClassName == null) {
               callerClassName = ste.getClassName();
            } else if (!callerClassName.equals(ste.getClassName())) {
               return ste.getClassName();
            }
         }
      }

      return null;
   }

   @OnlyIn(Dist.CLIENT)
   @Nullable
   public static LivingEntity getStackHolder(ItemStack stack) {
      return ((ItemStackClientExtensor)(Object)stack).getCachedHolder();
   }
}
