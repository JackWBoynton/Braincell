package bottomtextdanny.braincell.mod._base.blitty;

import bottomtextdanny.braincell.mod._base.blitty.color_data.BlittyColor;
import bottomtextdanny.braincell.mod._base.blitty.pos_data.BlittyPos;
import bottomtextdanny.braincell.mod._base.blitty.uv_data.BlittyUV;
import org.jetbrains.annotations.Nullable;

public class PixelCutter implements BlittyConfig {
   public final BlittyVector vector;
   private float pixels;

   public PixelCutter(BlittyVector vector) {
      this.vector = vector;
   }

   public void start(Blitty blitty) {
   }

   public void vertex(Blitty blitty, @Nullable BlittyColor color, BlittyPos position) {
      float relativeTransition;
      float pixelRelative;
      if (this.vector.usesX()) {
         relativeTransition = (float)this.vector.x >= 0.0F ? position.transitionX : 1.0F - position.transitionX;
         pixelRelative = relativeTransition / (float)blitty.width();
         position.x -= pixelRelative * (float)this.vector.x * this.pixels * 2.0F;
      } else if (this.vector.usesY()) {
         relativeTransition = (float)this.vector.y >= 0.0F ? position.transitionY : 1.0F - position.transitionY;
         pixelRelative = relativeTransition / (float)blitty.height();
         position.y -= pixelRelative * (float)this.vector.y * this.pixels * 2.0F;
      }

   }

   public void finish(Blitty blitty, BlittyUV uv) {
      if (this.vector.usesX()) {
         if ((float)this.vector.x <= 0.0F) {
            uv.x += this.pixels;
         }

         uv.width -= this.pixels;
      } else if (this.vector.usesY()) {
         if ((float)this.vector.y <= 0.0F) {
            uv.y += this.pixels;
         }

         uv.height -= this.pixels;
      }

   }

   public void setCutoffPixels(float pixels) {
      this.pixels = pixels;
   }

   public void setCutoffPixels(int max, float normalizedPixelRange) {
      this.pixels = (float)max * normalizedPixelRange;
   }

   public boolean usesColor() {
      return false;
   }

   public boolean usesPos() {
      return true;
   }

   public boolean usesUV() {
      return true;
   }
}
