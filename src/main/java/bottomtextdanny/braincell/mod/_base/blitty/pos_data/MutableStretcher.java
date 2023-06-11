package bottomtextdanny.braincell.mod._base.blitty.pos_data;

public class MutableStretcher implements BlittyPosTransformer {
   private float stretchX;
   private float stretchY;

   public MutableStretcher() {
      this(1.0F, 1.0F);
   }

   public MutableStretcher(float x, float y) {
      this.stretchX = x;
      this.stretchY = y;
   }

   public void transform(BlittyPos pos) {
      pos.x *= this.stretchX;
      pos.y *= this.stretchY;
   }
}
