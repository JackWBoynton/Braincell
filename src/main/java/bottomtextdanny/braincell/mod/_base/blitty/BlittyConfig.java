package bottomtextdanny.braincell.mod._base.blitty;

import bottomtextdanny.braincell.mod._base.blitty.color_data.BlittyColor;
import bottomtextdanny.braincell.mod._base.blitty.pos_data.BlittyPos;
import bottomtextdanny.braincell.mod._base.blitty.uv_data.BlittyUV;
import javax.annotation.Nullable;

public interface BlittyConfig {
   void start(Blitty var1);

   void vertex(Blitty var1, @Nullable BlittyColor var2, BlittyPos var3);

   void finish(Blitty var1, BlittyUV var2);

   boolean usesColor();

   boolean usesPos();

   boolean usesUV();
}
