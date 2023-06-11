package bottomtextdanny.braincell.mod._base.blitty.uv_data;

import bottomtextdanny.braincell.base.screen.ImageBounds;

public class BlittyUV {
   public final ImageBounds image;
   public final float originalX;
   public final float originalY;
   public final float originalWidth;
   public final float originalHeight;
   public float x;
   public float width;
   public float y;
   public float height;

   public BlittyUV(ImageBounds image, float x, float y, float width, float height) {
      this.image = image;
      this.originalX = x;
      this.originalY = y;
      this.originalWidth = width;
      this.originalHeight = height;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }
}
