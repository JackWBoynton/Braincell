package bottomtextdanny.braincell.libraries.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.Objects;
import java.util.function.Supplier;

public class RegistryHelper<T> {
    private final ModDeferringManager state;
    private final BCRegistry<T> registry;

    public RegistryHelper(ModDeferringManager state, BCRegistry<T> registry) {
        super();
        this.state = state;
        this.registry = registry;
    }

    public <U extends Wrap<? extends T>> U deferWrap(U sup) {
        Objects.requireNonNull(sup);
        sup.setupForDeferring(this.state);
        this.registry.addDeferredSolving(sup);
        return sup;
    }

    public <U extends T> Wrap<U> defer(String name, Supplier<U> sup) {
        Objects.requireNonNull(sup);
        Wrap<U> wrapped = new Wrap<>(new ResourceLocation(this.state.getModID(), name), sup);
        wrapped.setupForDeferring(this.state);
        this.registry.addDeferredSolving(wrapped);
        return wrapped;
    }

    public <U extends T> U makeSideRegistry(String name, U object) {
        Objects.requireNonNull(object);
        registry().addDeferredRegistry(new ResourceLocation(this.state.getModID(), name), () -> object);
        return object;
    }

    public ModDeferringManager state() {
        return this.state;
    }

    public BCRegistry<T> registry() {
        return this.registry;
    }
}
