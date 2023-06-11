package bottomtextdanny.braincell.mod._base.blitty.uv_data;

public class UVShifter implements BlittyUVPosTransformer {
   public float advanceX;
   public float advanceY;
   private float time;

   public UVShifter(float advanceX, float advanceY) {
      this.advanceX = advanceX;
      this.advanceY = advanceY;
   }

   public void updateTime(float time) {
      this.time += time;
   }

   public void transform(BlittyUV pos) {
      pos.x += this.advanceX * this.time * pos.width % (float)pos.image.width();
      pos.y += this.advanceY * this.time * pos.height % (float)pos.image.height();
   }
}
