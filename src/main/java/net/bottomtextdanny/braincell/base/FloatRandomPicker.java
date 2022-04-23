package net.bottomtextdanny.braincell.base;

import java.util.random.RandomGenerator;

@FunctionalInterface
public interface FloatRandomPicker {

    float compute(float min, float max, RandomGenerator random);

    static FloatRandomPicker linear() {
        return (min, max, random) -> {
            return random.nextFloat(min, max);
        };
    }

    static FloatRandomPicker trapezoid() {
        return (min, max, random) -> {
            float ran = max - min;
            float cen = ran / 2.0F;
            float inv = ran - cen;
            return min + random.nextFloat() * inv + random.nextFloat() * cen;
        };
    }

    static FloatRandomPicker trapezoid(float plateau) {
        return (min, max, random) -> {
            float ran = max - min;
            float cen = (ran - plateau) / 2.0F;
            float inv = ran - cen;
            return min + random.nextFloat() * inv + random.nextFloat() * cen;
        };
    }

    static FloatRandomPicker normal() {
        return (min, max, random) -> {
            float mean = (max - min) / 2.0F;
            return min + (float)random.nextGaussian() * mean;
        };
    }

}
