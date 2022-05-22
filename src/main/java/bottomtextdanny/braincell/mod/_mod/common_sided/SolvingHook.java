package bottomtextdanny.braincell.mod._mod.common_sided;

import bottomtextdanny.braincell.mod._base.registry.managing.ModDeferringManager;

import java.util.Objects;

@FunctionalInterface
public interface SolvingHook<T> {

    void execute(T object, ModDeferringManager solving);

    default SolvingHook<T> append(SolvingHook<T> after) {
        Objects.requireNonNull(after);
        return (object, solving) -> {
            execute(object, solving);
            after.execute(object, solving);
        };
    }
}
