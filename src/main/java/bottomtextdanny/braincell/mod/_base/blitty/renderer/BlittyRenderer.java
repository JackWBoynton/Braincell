package bottomtextdanny.braincell.mod._base.blitty.renderer;

import bottomtextdanny.braincell.mod._base.blitty.Blitty;
import bottomtextdanny.braincell.mod._base.blitty.BlittyConfig;
import com.mojang.blaze3d.vertex.PoseStack;

public interface BlittyRenderer {
   void render(Blitty var1, PoseStack var2, float var3, float var4, float var5, BlittyConfig... var6);

   static boolean modsColor(BlittyConfig... configurations) {
      if (configurations.length > 5) {
         return true;
      } else {
         BlittyConfig[] var1 = configurations;
         int var2 = configurations.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            BlittyConfig configuration = var1[var3];
            if (configuration.usesColor()) {
               return true;
            }
         }

         return false;
      }
   }

   static boolean modsPosition(BlittyConfig... configurations) {
      BlittyConfig[] var1 = configurations;
      int var2 = configurations.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         BlittyConfig configuration = var1[var3];
         if (configuration.usesPos()) {
            return true;
         }
      }

      return false;
   }

   static boolean modsUV(BlittyConfig... configurations) {
      BlittyConfig[] var1 = configurations;
      int var2 = configurations.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         BlittyConfig configuration = var1[var3];
         if (configuration.usesUV()) {
            return true;
         }
      }

      return false;
   }
}
