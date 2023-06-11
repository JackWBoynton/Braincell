package bottomtextdanny.braincell.mod._mod.client_sided;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class BCInputHelper {
   private static final BCClientToken TOKEN = new BCClientToken();

   public static boolean isShiftDown() {
      return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 340) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 344);
   }

   public static boolean isCTRLDown() {
      return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 341) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 345);
   }

   public static boolean isAltDown() {
      return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 342) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 346);
   }
}
