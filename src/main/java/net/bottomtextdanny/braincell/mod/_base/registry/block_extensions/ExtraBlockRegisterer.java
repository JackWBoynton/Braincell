package net.bottomtextdanny.braincell.mod._base.registry.block_extensions;

import net.bottomtextdanny.braincell.mod._base.registry.managing.ModDeferringManager;

public interface ExtraBlockRegisterer<T> {

    void registerExtra(T object, ModDeferringManager solving);

    default boolean executeSideRegistry() {
        return true;
    }
}
