package bottomtextdanny.braincell.libraries.psyche.input;

import java.util.function.Function;

public interface FunctionalActionInput<T, R> extends ActionInput, Function<T, R> {
}
