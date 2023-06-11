package bottomtextdanny.braincell.base.pair;

public record FloatPair(Float left, Object right) implements Tuple {
   public FloatPair(Float left, Object right) {
      this.left = left;
      this.right = right;
   }

   public static FloatPair of(float left, Object right) {
      return new FloatPair(left, right);
   }

   public Float left() {
      return this.left;
   }

   public Object right() {
      return this.right;
   }
}
