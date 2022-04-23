package net.bottomtextdanny.braincell.mod._base.blitty.pos_data;

import net.bottomtextdanny.braincell.mod._base.blitty.Blitty;
import net.bottomtextdanny.braincell.mod._base.blitty.BlittyConfig;
import net.bottomtextdanny.braincell.mod._base.blitty.BlittyTransformer;
import net.bottomtextdanny.braincell.mod._base.blitty.color_data.BlittyColor;
import net.bottomtextdanny.braincell.mod._base.blitty.uv_data.BlittyUV;

import javax.annotation.Nullable;

public interface BlittyPosTransformer extends BlittyTransformer, BlittyConfig {

    void transform(BlittyPos pos);

    @Override
    default void vertex(Blitty blitty, @Nullable BlittyColor color, BlittyPos position) {
        transform(position);
    }

    @Override
    default void finish(Blitty blitty, BlittyUV uv) {}

    @Override
    default boolean usesColor() {
        return false;
    }

    @Override
    default boolean usesPos() {
        return true;
    }

    @Override
    default boolean usesUV() {
        return false;
    }
}
