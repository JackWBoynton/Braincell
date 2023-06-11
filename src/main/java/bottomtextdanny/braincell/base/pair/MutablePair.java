package bottomtextdanny.braincell.base.pair;

public class MutablePair implements Tuple {
   private Object left;
   private Object right;

   public MutablePair(Object leftValue, Object rightValue) {
      this.left = leftValue;
      this.right = rightValue;
   }

   public Object left() {
      return this.left;
   }

   public Object right() {
      return this.right;
   }

   public void setLeft(Object left) {
      this.left = left;
   }

   public void setRight(Object right) {
      this.right = right;
   }
}
