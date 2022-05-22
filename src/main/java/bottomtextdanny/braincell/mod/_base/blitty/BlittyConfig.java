package bottomtextdanny.braincell.mod._base.blitty;

import bottomtextdanny.braincell.mod._base.blitty.color_data.BlittyColor;
import bottomtextdanny.braincell.mod._base.blitty.pos_data.BlittyPos;
import bottomtextdanny.braincell.mod._base.blitty.uv_data.BlittyUV;

import javax.annotation.Nullable;

public interface BlittyConfig {

    void start(Blitty blitty);

    void vertex(Blitty blitty, @Nullable BlittyColor color, BlittyPos position);

    void finish(Blitty blitty, BlittyUV uv);

    boolean usesColor();

    boolean usesPos();

    boolean usesUV();
}
