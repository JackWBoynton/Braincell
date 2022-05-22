package bottomtextdanny.braincell.mod._base.blitty.pos_data;

import bottomtextdanny.braincell.mod._base.blitty.Blitty;
import bottomtextdanny.braincell.mod._base.blitty.color_data.BlittyColor;
import bottomtextdanny.braincell.mod._base.blitty.BlittyConfig;
import bottomtextdanny.braincell.mod._base.blitty.BlittyTransformer;
import bottomtextdanny.braincell.mod._base.blitty.uv_data.BlittyUV;

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
