package bottomtextdanny.braincell.base.pair;

public record BytePair(Byte left, Object right) implements Tuple {
   public BytePair(Byte left, Object right) {
      this.left = left;
      this.right = right;
   }

   public static BytePair of(byte left, Object right) {
      return new BytePair(left, right);
   }

   public Byte left() {
      return this.left;
   }

   public Object right() {
      return this.right;
   }
}
