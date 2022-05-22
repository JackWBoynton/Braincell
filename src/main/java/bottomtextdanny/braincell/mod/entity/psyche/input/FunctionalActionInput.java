package bottomtextdanny.braincell.mod.entity.psyche.input;

import java.util.function.Function;

public interface FunctionalActionInput<T, R> extends ActionInput, Function<T, R> {
}
