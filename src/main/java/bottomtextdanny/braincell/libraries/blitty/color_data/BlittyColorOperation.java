package bottomtextdanny.braincell.libraries.blitty.color_data;

import java.util.function.BiFunction;

public enum BlittyColorOperation {
    ADDITIVE((f, s) -> f + s),
    MULTIPLICATIVE((f, s) -> f * s),
    SETTER((f, s) -> s);

    public final BiFunction<Float, Float, Float> func;

    BlittyColorOperation(BiFunction<Float, Float, Float> operation) {
        this.func = operation;
    }
}
