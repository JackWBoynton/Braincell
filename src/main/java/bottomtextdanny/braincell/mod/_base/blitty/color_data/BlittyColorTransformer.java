package bottomtextdanny.braincell.mod._base.blitty.color_data;

import bottomtextdanny.braincell.mod._base.blitty.Blitty;
import bottomtextdanny.braincell.mod._base.blitty.BlittyTransformer;
import bottomtextdanny.braincell.mod._base.blitty.pos_data.BlittyPos;
import bottomtextdanny.braincell.mod._base.blitty.uv_data.BlittyUV;
import javax.annotation.Nullable;

public interface BlittyColorTransformer extends BlittyTransformer {
   void transform(BlittyColor var1);

   default void vertex(Blitty blitty, @Nullable BlittyColor color, BlittyPos position) {
      this.transform(color);
   }

   default void finish(Blitty blitty, BlittyUV uv) {
   }

   default boolean usesColor() {
      return true;
   }

   default boolean usesPos() {
      return false;
   }

   default boolean usesUV() {
      return false;
   }
}
