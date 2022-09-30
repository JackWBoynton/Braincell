package bottomtextdanny.braincell.libraries.blitty.pos_data;

public class MutableStretcher implements BlittyPosTransformer {
    private float stretchX, stretchY;

    public MutableStretcher() {
        this(1.0F, 1.0F);
    }

    public MutableStretcher(float x, float y) {
        super();
        this.stretchX = x;
        this.stretchY = y;
    }

    @Override
    public void transform(BlittyPos pos) {
        pos.x *= this.stretchX;
        pos.y *= this.stretchY;
    }
}
