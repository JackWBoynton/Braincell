package net.bottomtextdanny.braincell.mod._mod.common_sided;

import com.google.common.collect.Maps;
import net.bottomtextdanny.braincell.Braincell;
import net.bottomtextdanny.braincell.mod.network.Connection;
import net.bottomtextdanny.braincell.mod._base.registry.block_extensions.BCBlockSolvingHook;
import net.bottomtextdanny.braincell.mod._base.registry.item_extensions.ExtraItemModelLoader;
import net.bottomtextdanny.braincell.mod._base.registry.managing.DeferrorType;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Map;

public final class DefaultSolvingHooks {
    private final Map<DeferrorType<?>, SolvingHook<?>> hooks;

    public DefaultSolvingHooks() {
        super();
        this.hooks = Maps.newIdentityHashMap();
        addHook(DeferrorType.BLOCK, new BCBlockSolvingHook<>());
        Connection.doClientSide(() -> {
            addHook(DeferrorType.ITEM, (item, solving) -> {
                if (item instanceof ExtraItemModelLoader modelLoader) {
                    Braincell.client().getExtraModelLoaders().addModelLoader(modelLoader);
                }
            });
        });
    }

    private <T extends IForgeRegistryEntry<T>> void addHook(DeferrorType<T> type, SolvingHook<T> hook) {
        if (this.hooks.containsKey(type)) {
            this.hooks.put(type, ((SolvingHook<T>)this.hooks.get(type)).append(hook));
        } else {
            this.hooks.put(type, hook);
        }
    }

    @Nullable
    public <T extends IForgeRegistryEntry<T>> SolvingHook<T> getHook(DeferrorType<T> type) {
        if (this.hooks.containsKey(type)) {
            return (SolvingHook<T>) this.hooks.get(type);
        } else {
            return null;
        }
    }
}
