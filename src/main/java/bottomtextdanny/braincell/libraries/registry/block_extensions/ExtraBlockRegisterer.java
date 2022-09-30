package bottomtextdanny.braincell.libraries.registry.block_extensions;

import bottomtextdanny.braincell.libraries.registry.ModDeferringManager;
import net.minecraft.resources.ResourceLocation;

public interface ExtraBlockRegisterer<T> {

    void registerExtra(T object, ResourceLocation key, ModDeferringManager solving);

    default boolean executeSideRegistry() {
        return true;
    }
}
