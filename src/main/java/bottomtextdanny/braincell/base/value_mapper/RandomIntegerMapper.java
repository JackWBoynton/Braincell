package bottomtextdanny.braincell.base.value_mapper;

import net.minecraft.util.RandomSource;

public abstract class RandomIntegerMapper {

    private RandomIntegerMapper() {}

    public static RandomIntegerMapper of(int minimum, int maximum) {
        return new RangedInteger(minimum, maximum);
    }

    public static RandomIntegerMapper of(int value) {
        return new FakeRangedInteger(value);
    }

    public abstract int map(RandomSource random);

    private static class FakeRangedInteger extends RandomIntegerMapper {
        private final int value;

        private FakeRangedInteger(int value) {
            this.value = value;
        }

        @Override
        public int map(RandomSource random) {
            return this.value;
        }
    }

    private static class RangedInteger extends RandomIntegerMapper {
        private final int minimum;
        private final int maximum;

        private RangedInteger(int minimum, int maximum) {
            this.minimum = minimum;
            this.maximum = maximum;
        }

        public int map(RandomSource random) {
            return this.minimum + random.nextInt(this.maximum - this.minimum);
        }
    }
}
