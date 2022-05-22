package bottomtextdanny.braincell.base;

import java.util.function.Function;
import java.util.function.Supplier;

public final class BCSuppliers {

    public static <T, R> Supplier<R> compose(Function<T, R> func, T unpacker) {
        return () -> func.apply(unpacker);
    }

    private BCSuppliers() {}
}
