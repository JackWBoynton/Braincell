package net.bottomtextdanny.braincell.mod._base.blitty.uv_data;

import net.bottomtextdanny.braincell.mod._base.blitty.Blitty;
import net.bottomtextdanny.braincell.mod._base.blitty.BlittyConfig;
import net.bottomtextdanny.braincell.mod._base.blitty.BlittyTransformer;
import net.bottomtextdanny.braincell.mod._base.blitty.color_data.BlittyColor;
import net.bottomtextdanny.braincell.mod._base.blitty.pos_data.BlittyPos;

import javax.annotation.Nullable;

public interface BlittyUVPosTransformer extends BlittyTransformer, BlittyConfig {

    void transform(BlittyUV pos);

    @Override
    default void vertex(Blitty blitty, @Nullable BlittyColor color, BlittyPos position) {}

    @Override
    default void finish(Blitty blitty, BlittyUV uv) {
        transform(uv);
    }

    @Override
    default boolean usesColor() {
        return false;
    }

    @Override
    default boolean usesPos() {
        return false;
    }

    @Override
    default boolean usesUV() {
        return true;
    }
}
