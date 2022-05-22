package bottomtextdanny.braincell.mod._base.blitty.uv_data;

import bottomtextdanny.braincell.base.screen.ImageBounds;

public class BlittyUV {
    public final ImageBounds image;
    public final float originalX, originalY, originalWidth, originalHeight;
    public float x, width;
    public float y, height;

    public BlittyUV(ImageBounds image, float x, float y, float width, float height) {
        super();
        this.image = image;
        this.originalX = x;
        this.originalY = y;
        this.originalWidth = width;
        this.originalHeight = height;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}

