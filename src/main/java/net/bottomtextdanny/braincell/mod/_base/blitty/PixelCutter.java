package net.bottomtextdanny.braincell.mod._base.blitty;

import net.bottomtextdanny.braincell.mod._base.blitty.pos_data.BlittyPos;
import net.bottomtextdanny.braincell.mod._base.blitty.color_data.BlittyColor;
import net.bottomtextdanny.braincell.mod._base.blitty.uv_data.BlittyUV;
import org.jetbrains.annotations.Nullable;

public class PixelCutter implements BlittyConfig {
    public final BlittyVector vector;
    private float pixels;

    public PixelCutter(BlittyVector vector) {
        this.vector = vector;
    }

    @Override
    public void start(Blitty blitty) {}

    @Override
    public void vertex(Blitty blitty, @Nullable BlittyColor color, BlittyPos position) {
        if (this.vector.usesX()) {
            final float relativeTransition = this.vector.x >= 0.0F ? position.transitionX : 1.0F - position.transitionX;
            final float pixelRelative =  relativeTransition / blitty.width();
            position.x -= pixelRelative * this.vector.x * this.pixels * 2;
        } else if (this.vector.usesY()) {
            final float relativeTransition = this.vector.y >= 0.0F ? position.transitionY : 1.0F - position.transitionY;
            final float pixelRelative = relativeTransition / blitty.height();
            position.y -= pixelRelative * this.vector.y * this.pixels * 2;
        }
    }

    @Override
    public void finish(Blitty blitty, BlittyUV uv) {
        if (this.vector.usesX()) {
            if (this.vector.x <= 0.0F) {
                uv.x += this.pixels;
            }
            uv.width -= this.pixels;
        } else if (this.vector.usesY()) {
            if (this.vector.y <= 0.0F) {
                uv.y += this.pixels;
            }
            uv.height -= this.pixels;
        }
    }

    public void setCutoffPixels(float pixels) {
        this.pixels = pixels;
    }

    public void setCutoffPixels(int max, float normalizedPixelRange) {
        this.pixels = max * normalizedPixelRange;
    }

    @Override
    public boolean usesColor() {
        return false;
    }

    @Override
    public boolean usesPos() {
        return true;
    }

    @Override
    public boolean usesUV() {
        return true;
    }
}
