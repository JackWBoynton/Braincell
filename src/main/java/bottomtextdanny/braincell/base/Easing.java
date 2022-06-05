package bottomtextdanny.braincell.base;

/*
https://easings.net/#
*/
public interface Easing {
    Easing LINEAR = value -> value;

    Easing EASE_IN_SINE = value -> 1 - BCMath.cos((float) (value * BCMath.PI_BY_TWO));

    Easing EASE_OUT_SINE = value -> BCMath.sin((float) (value * BCMath.PI_BY_TWO));

    Easing EASE_IN_OUT_SINE = value -> -(BCMath.cos(Math.PI * value) - 1) / 2;

    Easing EASE_IN_SQUARE = value -> value * value;

    Easing EASE_OUT_SQUARE = value -> {
        float inv = 1 - value;
        return 1 - inv * inv;
    };

    Easing EASE_IN_OUT_SQUARE = value -> {
        if (value < 0.5) {
            return 2 * value * value;
        } else {
            value = -2 * value + 2;
            return 1 - value * value / 2;
        }
    };

    Easing EASE_IN_CUBIC = value -> value * value * value;

    Easing EASE_OUT_CUBIC = value -> {
        value = 1 - value;
        return 1 - value * value * value;
    };

    Easing EASE_IN_OUT_CUBIC = value -> {
        if (value < 0.5) {
            return 4 * value * value * value;
        } else {
            value = -2 * value + 2;
            return 1 - value * value * value / 2;
        }
    };

    Easing EASE_IN_QUART = value -> value * value * value * value;

    Easing EASE_OUT_QUART = value -> {
        value = 1 - value;
        return 1 - value * value * value * value;
    };

    Easing EASE_IN_OUT_QUART = value -> {
        if (value < 0.5) {
            return 8 * value * value * value * value;
        } else {
            value = -2 * value + 2;
            return 1 - value * value * value * value / 2;
        }
    };

    Easing EASE_IN_QUINT = value -> value * value * value * value * value;

    Easing EASE_OUT_QUINT = value -> {
        value = 1 - value;
        return 1 - value * value * value * value * value;
    };

    Easing EASE_IN_OUT_QUINT = value -> {
        if (value < 0.5) {
            return 16 * value * value * value * value * value;
        } else {
            value = -2 * value + 2;
            return 1 - value * value * value * value * value / 2;
        }
    };

    Easing BOUNCE_OUT = value -> {
        double n1 = 7.5625;
        double d1 = 2.75;

        if (value < 1 / d1) {
            return (float) (n1 * value * value);
        } else if (value < 2 / d1) {
            return (float) (n1 * (value -= 1.5 / d1) * value + 0.75);
        } else if (value < 2.5 / d1) {
            return (float) (n1 * (value -= 2.25 / d1) * value + 0.9375);
        } else {
            return (float) (n1 * (value -= 2.625 / d1) * value + 0.984375);
        }
    };

    Easing BOUNCE_IN = value -> {
        return 1 - BOUNCE_OUT.progression(value);
    };

    Easing BOUNCE_IN_OUT = value -> {
        return value < 0.5F
                ? (1 - BOUNCE_OUT.progression(1 - 2 * value)) / 2
                : (1 + BOUNCE_OUT.progression(2 * value - 1)) / 2;
    };

    Easing EASE_IN_GAMMA = value -> (float) Math.pow(value, 2.2F);
    Easing EASE_OUT_GAMMA = value -> (float) (1 - Math.pow(1 - value, 2.2F));

    Easing EASE_IN_BACK = value -> {
        double c1 = 1.70158;
        double c3 = c1 + 1;

        return (float) (1 + c3 * Math.pow(value - 1, 3) + c1 * Math.pow(value - 1, 2));
    };

    Easing EASE_OUT_BACK = value -> {
        double c1 = 1.70158;
        double c3 = c1 + 1;

        return (float) (c3 * value * value * value - c1 * value * value);
    };

    float progression(float value);
}
