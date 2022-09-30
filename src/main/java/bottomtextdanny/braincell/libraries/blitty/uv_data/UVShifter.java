package bottomtextdanny.braincell.libraries.blitty.uv_data;

public class UVShifter implements BlittyUVPosTransformer {
    public float advanceX;
    public float advanceY;
    private float time;

    public UVShifter(float advanceX, float advanceY) {
        super();
        this.advanceX = advanceX;
        this.advanceY = advanceY;
    }

    public void updateTime(float time) {
        this.time += time;
    }

    @Override
    public void transform(BlittyUV pos) {
        pos.x += (this.advanceX * this.time * pos.width) % pos.image.width();
        pos.y += (this.advanceY * this.time * pos.height) % pos.image.height();
    }
}
