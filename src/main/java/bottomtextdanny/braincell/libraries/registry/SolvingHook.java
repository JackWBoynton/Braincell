package bottomtextdanny.braincell.libraries.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

@FunctionalInterface
public interface SolvingHook<T> {

    void execute(ResourceLocation key, T object, ModDeferringManager solving);

    default SolvingHook<T> append(SolvingHook<T> after) {
        Objects.requireNonNull(after);
        return (key, object, solving) -> {
            execute(key, object, solving);
            after.execute(key, object, solving);
        };
    }
}
