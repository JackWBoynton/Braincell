package net.bottomtextdanny.braincell.mod._base.blitty.pos_data;

public class BlittyPos {
    public final float transitionX;
    public final float transitionY;
    public final float originalX;
    public final float originalY;
    public float x;
    public float y;

    public BlittyPos(float transitionX, float transitionY, float x, float y) {
        this.transitionX = transitionX;
        this.transitionY = transitionY;
        this.originalX = x;
        this.originalY = y;
        this.x = x;
        this.y = y;
    }
}
