package bottomtextdanny.braincell.libraries.blitty;

import bottomtextdanny.braincell.libraries.blitty.color_data.BlittyColor;
import bottomtextdanny.braincell.libraries.blitty.color_data.BlittyColorTransformer;
import bottomtextdanny.braincell.libraries.blitty.pos_data.BlittyPos;
import bottomtextdanny.braincell.libraries.blitty.pos_data.BlittyPosTransformer;
import bottomtextdanny.braincell.libraries.blitty.uv_data.BlittyUV;
import bottomtextdanny.braincell.libraries.blitty.uv_data.BlittyUVPosTransformer;

import javax.annotation.Nullable;

public class BlittyImmutableConfig implements BlittyConfig {
    @Nullable
    private final BlittyColorTransformer colorTransformer;
    @Nullable
    private final BlittyPosTransformer posTransformer;
    @Nullable
    private final BlittyUVPosTransformer uvTransformer;

    public BlittyImmutableConfig(
            @Nullable BlittyColorTransformer colorTransformer,
            @Nullable BlittyPosTransformer posTransformer,
            @Nullable BlittyUVPosTransformer uvTransformer) {
        this.colorTransformer = colorTransformer;
        this.posTransformer = posTransformer;
        this.uvTransformer = uvTransformer;
    }

    @Override
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

    @Override
    public void vertex(Blitty blitty, @Nullable BlittyColor color, BlittyPos position) {
        if (this.colorTransformer != null) {
            this.colorTransformer.transform(color);
        }
        if (this.posTransformer != null) {
            this.posTransformer.transform(position);
        }
    }

    @Override
    public void finish(Blitty blitty, BlittyUV uv) {
        if (this.uvTransformer != null) {
            this.uvTransformer.transform(uv);
        }
    }

    @Override
    public boolean usesColor() {
        return this.colorTransformer != null;
    }

    @Override
    public boolean usesPos() {
        return this.posTransformer != null;
    }

    @Override
    public boolean usesUV() {
        return this.uvTransformer != null;
    }
}
