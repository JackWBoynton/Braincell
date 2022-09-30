package bottomtextdanny.braincell.libraries.registry;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.registry.block_extensions.BCBlockSolvingHook;
import bottomtextdanny.braincell.libraries.network.Connection;
import com.google.common.collect.Maps;
import bottomtextdanny.braincell.libraries.registry.item_extensions.ExtraItemModelLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import javax.annotation.Nullable;
import java.util.Map;

public final class DefaultSolvingHooks {
    private final Map<ResourceKey<? extends Registry<?>>, SolvingHook<?>> hooks;

    public DefaultSolvingHooks() {
        super();
        this.hooks = Maps.newIdentityHashMap();
        addHook(Registry.BLOCK.key(), new BCBlockSolvingHook<>());
        Connection.doClientSide(() -> {
            addHook(Registry.ITEM.key(), (key, item, solving) -> {
                if (item instanceof ExtraItemModelLoader modelLoader) {
                    Braincell.client().getExtraModelLoaders().addModelLoader(key, modelLoader);
                }
            });
        });
    }

    private <T> void addHook(ResourceKey<? extends Registry<T>> type, SolvingHook<T> hook) {
        if (this.hooks.containsKey(type)) {
            this.hooks.put(type, ((SolvingHook<T>)this.hooks.get(type)).append(hook));
        } else {
            this.hooks.put(type, hook);
        }
    }

    @Nullable
    public <T> SolvingHook<T> getHook(ResourceKey<? extends Registry<T>> type) {
        if (this.hooks.containsKey(type)) {
            return (SolvingHook<T>) this.hooks.get(type);
        } else {
            return null;
        }
    }
}
