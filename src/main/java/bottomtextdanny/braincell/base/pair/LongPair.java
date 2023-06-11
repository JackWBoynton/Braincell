package bottomtextdanny.braincell.base.pair;

public record LongPair(Long left, Object right) implements Tuple {
   public LongPair(Long left, Object right) {
      this.left = left;
      this.right = right;
   }

   public static LongPair of(long left, Object right) {
      return new LongPair(left, right);
   }

   public Long left() {
      return this.left;
   }

   public Object right() {
      return this.right;
   }
}
