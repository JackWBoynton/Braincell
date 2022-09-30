package bottomtextdanny.braincell.libraries.registry.block_extensions;

import bottomtextdanny.braincell.libraries._minor.chest.BCChestBlock;
import bottomtextdanny.braincell.libraries.registry.BCRegistry;
import bottomtextdanny.braincell.libraries.registry.ModDeferringManager;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public interface ExtraChestRegisterer<T extends BCChestBlock, U extends BCChestBlock> extends ExtraBlockRegisterer<T> {

    @Override
    default void registerExtra(T object, ResourceLocation key, ModDeferringManager solving) {
        Objects.requireNonNull(object);
        Optional<BCRegistry<Block>> registryOp = solving.getRegistryDeferror(Registry.BLOCK_REGISTRY);
        registryOp.ifPresent(registry -> {
            U newTrappedChestBlock = trappedFactory(object).get();
            ResourceLocation location = new ResourceLocation(solving.getModID(), "trapped_" + key.getPath());

            solving.doHooksForObject(Registry.BLOCK, location, newTrappedChestBlock);
            registry.addDeferredRegistry(location, () -> newTrappedChestBlock);
            setTrapped(newTrappedChestBlock);
        });
    }

    void setTrapped(U declaration);

    Supplier<U> trappedFactory(BCChestBlock base);

    U trapped();
}
