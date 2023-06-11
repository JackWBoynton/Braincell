package bottomtextdanny.braincell.mod._base.blitty;

import bottomtextdanny.braincell.mod._base.blitty.color_data.BlittyColor;
import bottomtextdanny.braincell.mod._base.blitty.color_data.BlittyColorTransformer;
import bottomtextdanny.braincell.mod._base.blitty.pos_data.BlittyPos;
import bottomtextdanny.braincell.mod._base.blitty.pos_data.BlittyPosTransformer;
import bottomtextdanny.braincell.mod._base.blitty.uv_data.BlittyUV;
import bottomtextdanny.braincell.mod._base.blitty.uv_data.BlittyUVPosTransformer;
import javax.annotation.Nullable;

public class BlittyImmutableConfig implements BlittyConfig {
   @Nullable
   private final BlittyColorTransformer colorTransformer;
   @Nullable
   private final BlittyPosTransformer posTransformer;
   @Nullable
   private final BlittyUVPosTransformer uvTransformer;

   public BlittyImmutableConfig(@Nullable BlittyColorTransformer colorTransformer, @Nullable BlittyPosTransformer posTransformer, @Nullable BlittyUVPosTransformer uvTransformer) {
      this.colorTransformer = colorTransformer;
      this.posTransformer = posTransformer;
      this.uvTransformer = uvTransformer;
   }

   public void start(Blitty blitty) {
      if (this.colorTransformer != null && this.colorTransformer instanceof BlittyStartCallout) {
         this.colorTransformer.start(blitty);
      }

      if (this.posTransformer != null && this.posTransformer instanceof BlittyStartCallout) {
         this.posTransformer.start(blitty);
      }

      if (this.uvTransformer != null && this.uvTransformer instanceof BlittyStartCallout) {
         this.uvTransformer.start(blitty);
      }

   }

   public void vertex(Blitty blitty, @Nullable BlittyColor color, BlittyPos position) {
      if (this.colorTransformer != null) {
         this.colorTransformer.transform(color);
      }

      if (this.posTransformer != null) {
         this.posTransformer.transform(position);
      }

   }

   public void finish(Blitty blitty, BlittyUV uv) {
      if (this.uvTransformer != null) {
         this.uvTransformer.transform(uv);
      }

   }

   public boolean usesColor() {
      return this.colorTransformer != null;
   }

   public boolean usesPos() {
      return this.posTransformer != null;
   }

   public boolean usesUV() {
      return this.uvTransformer != null;
   }
}
