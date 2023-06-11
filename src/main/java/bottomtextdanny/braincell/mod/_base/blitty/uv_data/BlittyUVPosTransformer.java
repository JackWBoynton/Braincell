package bottomtextdanny.braincell.mod._base.blitty.uv_data;

import bottomtextdanny.braincell.mod._base.blitty.Blitty;
import bottomtextdanny.braincell.mod._base.blitty.BlittyConfig;
import bottomtextdanny.braincell.mod._base.blitty.BlittyTransformer;
import bottomtextdanny.braincell.mod._base.blitty.color_data.BlittyColor;
import bottomtextdanny.braincell.mod._base.blitty.pos_data.BlittyPos;
import javax.annotation.Nullable;

public interface BlittyUVPosTransformer extends BlittyTransformer, BlittyConfig {
   void transform(BlittyUV var1);

   default void vertex(Blitty blitty, @Nullable BlittyColor color, BlittyPos position) {
   }

   default void finish(Blitty blitty, BlittyUV uv) {
      this.transform(uv);
   }

   default boolean usesColor() {
      return false;
   }

   default boolean usesPos() {
      return false;
   }

   default boolean usesUV() {
      return true;
   }
}
