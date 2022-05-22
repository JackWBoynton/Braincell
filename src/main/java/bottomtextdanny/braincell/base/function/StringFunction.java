package bottomtextdanny.braincell.base.function;

@FunctionalInterface
public interface StringFunction<T> {

    T apply(String value);
}
