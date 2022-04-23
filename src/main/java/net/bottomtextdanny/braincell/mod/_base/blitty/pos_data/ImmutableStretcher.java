package net.bottomtextdanny.braincell.mod._base.blitty.pos_data;

public record ImmutableStretcher(float stretchX, float stretchY) implements BlittyPosTransformer {

    @Override
    public void transform(BlittyPos pos) {
        pos.x *= this.stretchX;
        pos.y *= this.stretchY;
    }
}
