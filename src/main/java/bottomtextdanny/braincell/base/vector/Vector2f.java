package bottomtextdanny.braincell.base.vector;

public record Vector2f(float x, float y) {
   public Vector2f(float x, float y) {
      this.x = x;
      this.y = y;
   }

   public float x() {
      return this.x;
   }

   public float y() {
      return this.y;
   }
}
