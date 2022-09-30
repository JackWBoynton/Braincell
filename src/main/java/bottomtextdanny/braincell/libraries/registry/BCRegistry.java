package bottomtextdanny.braincell.libraries.registry;

import bottomtextdanny.braincell.base.pair.Pair;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegisterEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class BCRegistry<T> {
    private final List<Wrap<? extends T>> insideEntries = Lists.newLinkedList();
    private final List<Pair<ResourceLocation, Supplier<? extends T>>> registryEntries = Lists.newLinkedList();

    public BCRegistry() {
        super();
    }

    public void addDeferredSolving(Wrap<? extends T> objectSupplier) {
        this.insideEntries.add(objectSupplier);
        addDeferredRegistry(objectSupplier.key, objectSupplier);
    }

    public void addDeferredRegistry(ResourceLocation location, Supplier<? extends T> objectSupplier) {
        this.registryEntries.add(Pair.of(location, objectSupplier));
    }

    public final void registerAll(RegisterEvent.RegisterHelper<T> event) {
        this.registryEntries.forEach(entry -> {
            event.register(entry.left(), entry.right().get());
        });
    }

    public Collection<Wrap<? extends T>> getSolvingEntries() {
        return Collections.unmodifiableCollection(this.insideEntries);
    }

    public Collection<Pair<ResourceLocation, Supplier<? extends T>>> getRegistryEntries() {
        return Collections.unmodifiableCollection(this.registryEntries);
    }

}
