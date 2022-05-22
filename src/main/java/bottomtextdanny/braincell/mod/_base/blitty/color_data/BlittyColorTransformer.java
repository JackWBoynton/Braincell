package bottomtextdanny.braincell.mod._base.blitty.color_data;


import bottomtextdanny.braincell.mod._base.blitty.Blitty;
import bottomtextdanny.braincell.mod._base.blitty.BlittyTransformer;
import bottomtextdanny.braincell.mod._base.blitty.pos_data.BlittyPos;
import bottomtextdanny.braincell.mod._base.blitty.uv_data.BlittyUV;

import javax.annotation.Nullable;

public interface BlittyColorTransformer extends BlittyTransformer {

    void transform(BlittyColor color);

    @Override
    default void vertex(Blitty blitty, @Nullable BlittyColor color, BlittyPos position) {
        transform(color);
    }

    @Override
    default void finish(Blitty blitty, BlittyUV uv) {}

    @Override
    default boolean usesColor() {
        return true;
    }

    @Override
    default boolean usesPos() {
        return false;
    }

    @Override
    default boolean usesUV() {
        return false;
    }
}
