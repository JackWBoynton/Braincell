package bottomtextdanny.braincell.base.pair;

public record DoublePair(Double left, Object right) implements Tuple {
   public DoublePair(Double left, Object right) {
      this.left = left;
      this.right = right;
   }

   public static DoublePair of(double left, Object right) {
      return new DoublePair(left, right);
   }

   public Double left() {
      return this.left;
   }

   public Object right() {
      return this.right;
   }
}
