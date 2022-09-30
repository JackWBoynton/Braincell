package bottomtextdanny.braincell.libraries.registry;

import bottomtextdanny.braincell.Braincell;
import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public final class ModDeferringManager {
    public static final Supplier<UnsupportedOperationException> CANNOT_PERFORM_OPERATIONS_ON_CLOSED_STATE =
            () -> new UnsupportedOperationException("Attempted to perform conditional on closed solving block");
    private final LinkedHashMap<ResourceKey<? extends Registry<?>>, BCRegistry<?>> deferrorAccessors;
    private final Map<ResourceKey<? extends Registry<?>>, SolvingHook<?>> hooks;
    private final String modID;
    boolean open = true;

    public ModDeferringManager(String modID) {
        super();
        this.modID = modID;
        this.deferrorAccessors = Maps.newLinkedHashMap();
        this.hooks = Maps.newIdentityHashMap();
    }

    public <T> void addRegistryDeferror(ResourceKey<? extends Registry<T>> type, BCRegistry<T> registry) {
        if (this.open) {
            if (this.deferrorAccessors.containsKey(type)) {
                throw repeatedAccessorEntryException(type, this.modID);
            } else {
                this.deferrorAccessors.put(type, registry);
            }
        } else throw CANNOT_PERFORM_OPERATIONS_ON_CLOSED_STATE.get();
    }

    public <T> void addRegistryDeferror(Registry<T> type, BCRegistry<T> registry) {
        addRegistryDeferror(type.key(), registry);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<BCRegistry<T>> getRegistryDeferror(ResourceKey<? extends Registry<T>> type) {
        return Optional.ofNullable((BCRegistry<T>)this.deferrorAccessors.get(type));
    }

    public void solveAndLockForeverEver() {
        if (this.open) {
            IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
            GameData.unfreezeData();
            this.deferrorAccessors.forEach((type, registry) -> {
                solveDeferror(bus, type, registry);
            });
            this.open = false;
        }
    }

    public <T extends RegistryObject<T>> void addHook(ResourceKey<? extends Registry<T>> type, SolvingHook<T> hook) {
        if (this.open) {
            if (this.hooks.containsKey(type)) {
                this.hooks.put(type, ((SolvingHook<T>) this.hooks.get(type)).append(hook));
            } else {
                this.hooks.put(type, hook);
            }
        } else throw CANNOT_PERFORM_OPERATIONS_ON_CLOSED_STATE.get();
    }

    public <T extends RegistryObject<T>> void addHook(Registry<T> type, SolvingHook<T> hook) {
        addHook(type.key(), hook);
    }

    @Nullable
    public <T> SolvingHook<T> getHook(ResourceKey<? extends Registry<T>> type) {
        if (this.hooks.containsKey(type)) {
            return (SolvingHook<T>) this.hooks.get(type);
        } else {
            return null;
        }
    }

    public <T> void doHooksForObject(ResourceKey<? extends Registry<T>> type, ResourceLocation key, T object) {
        SolvingHook<T> defaultSolvingHook = Braincell.common().getSolvingHooks().getHook(type);
        SolvingHook<T> solvingHook = getHook(type);

        if (defaultSolvingHook != null) {
            defaultSolvingHook.execute(key, object, this);
        }

        if (solvingHook != null) {
            solvingHook.execute(key, object, this);
        }
    }

    public <T> void doHooksForObject(Registry<T> type, ResourceLocation key, T object) {
        doHooksForObject(type.key(), key, object);
    }

    private <T> void solveDeferror(IEventBus modEventBus,
                                   ResourceKey<? extends Registry<?>> rawType,
                                   BCRegistry<?> rawRegistry) {
        ResourceKey<? extends Registry<T>> type = (ResourceKey<? extends Registry<T>>)rawType;
        BCRegistry<T> registry = (BCRegistry<T>) rawRegistry;

        SolvingHook<T> solvingHook = getHook(type);
        if (solvingHook != null) {
            SolvingHook<T> defaultSolvingHook = Braincell.common().getSolvingHooks().getHook(type);
            if (defaultSolvingHook != null) {
                solvingHook = solvingHook.append(defaultSolvingHook);
            }
            SolvingHook<T> finalSolvingHook = solvingHook;
            Iterator<Wrap<? extends T>> iterator = registry.getSolvingEntries().iterator();
            while(iterator.hasNext()) {
                Wrap<? extends T> entry = iterator.next();

                entry.solve();
                if (entry.obj == null) {
                    throw new IllegalStateException("Solving Object shouldn't point null after solving process. " + entry.key.toString());
                }

                finalSolvingHook.execute(entry.key, entry.get(), this);
            }
        } else {
            solvingHook = Braincell.common().getSolvingHooks().getHook(type);
            Iterator<Wrap<? extends T>> iterator = registry.getSolvingEntries().iterator();
            while(iterator.hasNext()) {
                Wrap<? extends T> entry = iterator.next();

                entry.solve();
                if (entry.obj == null) {
                    throw new IllegalStateException("Solving Object shouldn't be null after solving process.");
                }

                if (solvingHook != null) {
                    solvingHook.execute(entry.key, entry.get(), this);
                }
            }
        }

        modEventBus.addListener((RegisterEvent event) -> {
            event.register(type, helper -> {
                registry.registerAll(helper);
            });
        });
      //  modEventBus.addGenericListener(registry::registerAll);
    }

    public boolean isOpen() {
        return this.open;
    }

    public String getModID() {
        return this.modID;
    }

    private static IllegalArgumentException repeatedAccessorEntryException(ResourceKey<? extends Registry<?>> accessorKey, String modId) {
        String message = new StringBuilder("Cannot assign value to accessor key because there is already one; Accessor key:")
                .append(accessorKey)
                .append(", Mod id:")
                .append(modId)
                .append('.')
                .toString();
        return new IllegalArgumentException(message);
    }
}
