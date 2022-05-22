package bottomtextdanny.braincell.base.function;

import java.util.function.Function;

public final class BCFunctions {

    public static <T, R> Function<T, R> supply(R value) {
        return t -> value;
    }

    private BCFunctions() {}
}
