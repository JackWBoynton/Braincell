package bottomtextdanny.braincell.mod._base.blitty.pos_data;

public record ImmutableStretcher(float stretchX, float stretchY) implements BlittyPosTransformer {
   public ImmutableStretcher(float stretchX, float stretchY) {
      this.stretchX = stretchX;
      this.stretchY = stretchY;
   }

   public void transform(BlittyPos pos) {
      pos.x *= this.stretchX;
      pos.y *= this.stretchY;
   }

   public float stretchX() {
      return this.stretchX;
   }

   public float stretchY() {
      return this.stretchY;
   }
}
