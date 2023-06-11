package bottomtextdanny.braincell.base.value_mapper;

import it.unimi.dsi.fastutil.floats.FloatFloatMutablePair;
import java.util.function.Consumer;
import java.util.random.RandomGenerator;

public final class FloatMappers {
   private FloatMappers() {
   }

   public static FloatMapper of(float minimum, float maximum) {
      return new RangedFloat(minimum, maximum);
   }

   public static FloatMapper of(float value) {
      return new FakeRangedFloat(value);
   }

   public static FloatMapper functionalOf(Consumer getter) {
      return new Functional(getter);
   }

   private static class RangedFloat implements FloatMapper {
      private final float minimum;
      private final float maximum;

      private RangedFloat(float minimum, float maximum) {
         this.minimum = minimum;
         this.maximum = maximum;
      }

      public float map(RandomGenerator random) {
         return this.minimum + random.nextFloat(this.maximum - this.minimum);
      }
   }

   private static class FakeRangedFloat implements FloatMapper {
      private final float value;

      private FakeRangedFloat(float value) {
         this.value = value;
      }

      public float map(RandomGenerator random) {
         return this.value;
      }
   }

   private static class Functional implements FloatMapper {
      private final FloatFloatMutablePair minMax = new FloatFloatMutablePair(0.0F, 0.0F);
      private final Consumer getter;

      private Functional(Consumer getter) {
         this.getter = getter;
      }

      public float map(RandomGenerator random) {
         this.getter.accept(this.minMax);
         return this.minMax.leftFloat() + random.nextFloat(this.minMax.rightFloat() - this.minMax.leftFloat());
      }
   }
}
