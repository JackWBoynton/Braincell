package bottomtextdanny.braincell.base.value_mapper;

import java.util.Random;
import java.util.SplittableRandom;

public abstract class RandomIntegerMapper {
   private RandomIntegerMapper() {
   }

   public static RandomIntegerMapper of(int minimum, int maximum) {
      return new RangedInteger(minimum, maximum);
   }

   public static RandomIntegerMapper of(int value) {
      return new FakeRangedInteger(value);
   }

   public abstract int map(SplittableRandom var1);

   public abstract int map(Random var1);

   private static class RangedInteger extends RandomIntegerMapper {
      private final int minimum;
      private final int maximum;

      private RangedInteger(int minimum, int maximum) {
         this.minimum = minimum;
         this.maximum = maximum;
      }

      public int map(SplittableRandom random) {
         return this.minimum + random.nextInt(this.maximum - this.minimum);
      }

      public int map(Random random) {
         return this.minimum + random.nextInt(this.maximum - this.minimum);
      }
   }

   private static class FakeRangedInteger extends RandomIntegerMapper {
      private final int value;

      private FakeRangedInteger(int value) {
         this.value = value;
      }

      public int map(SplittableRandom random) {
         return this.value;
      }

      public int map(Random random) {
         return this.value;
      }
   }
}
