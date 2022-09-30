package bottomtextdanny.braincell.base.scheduler;

import bottomtextdanny.braincell.base.FloatRandomPicker;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

import java.util.function.IntSupplier;

public abstract class IntScheduler implements Scheduler<Integer> {
    public static final RandomSource INNER_RANDOM = RandomSource.create();
    protected int current;

    public static Simple simple(int bound) {
        return new Simple(bound);
    }

    public static Variable variable(IntSupplier bound) {
        return new Variable(bound);
    }

    public static Ranged ranged(int min, int max) {
        return new Ranged(min, max);
    }

    public void incrementFreely(int step) {
        this.current = Math.min(step + this.current, bound());
    }

    public void hackyCounterSet(int current) {
        this.current = Mth.clamp(current, 0, bound());
    }

    @Override
    public void reset() {
        this.current = 0;
    }

    @Override
    public void end() {
        this.current = bound();
    }

    @Override
    public Integer current() {
        return this.current;
    }

    @Override
    public Integer bound() {
        return null;
    }

    @Override
    public boolean hasEnded() {
        return this.current >= bound();
    }

    public static final class Simple extends IntScheduler {
        private final int bound;

        public Simple(int bound) {
            this.bound = bound;
        }

        @Override
        public void advance() {
            if (this.current < this.bound)
                this.current++;
        }

        @Override
        public Integer bound() {
            return this.bound;
        }
    }

    public static final class Ranged extends IntScheduler {
        public final int minBound;
        public final int maxBound;
        private int currentBound;

        public Ranged(int minBound, int maxBound) {
            this.minBound = minBound;
            this.maxBound = maxBound;
        }

        public Ranged(int minBound, int maxBound, int currentBound) {
            this.minBound = minBound;
            this.maxBound = maxBound;
            this.currentBound = currentBound;
        }

        @Override
        public void advance() {
            if (this.current < this.currentBound)
                this.current++;
        }

        @Override
        public void reset() {
            super.reset();
            this.currentBound = INNER_RANDOM.nextInt(minBound, maxBound);
        }

        public void reset(FloatRandomPicker picker) {
            super.reset();
            this.currentBound = (int)picker.compute(minBound, maxBound, INNER_RANDOM);
        }

        @Override
        public Integer bound() {
            return this.currentBound;
        }
    }

    public static final class Variable extends IntScheduler {
        private final IntSupplier nextBoundSupplier;
        private int currentBound;

        private Variable(IntSupplier boundSupplier) {
            this.nextBoundSupplier = boundSupplier;
            this.currentBound = boundSupplier.getAsInt();
        }

        public Variable(IntSupplier boundSupplier, int currentBound, int current) {
            this.nextBoundSupplier = boundSupplier;
            this.currentBound = currentBound;
            this.current = current;
        }

        @Override
        public void reset() {
            super.reset();
            this.currentBound = this.nextBoundSupplier.getAsInt();
        }

        @Override
        public void advance() {
            if (this.current < this.currentBound)
                this.current++;
        }

        @Override
        public Integer bound() {
            return this.currentBound;
        }

        public void setCurrentBound(int currentBound) {
            this.currentBound = currentBound;
        }

        public IntSupplier getNextBoundSupplier() {
            return this.nextBoundSupplier;
        }
    }
}
