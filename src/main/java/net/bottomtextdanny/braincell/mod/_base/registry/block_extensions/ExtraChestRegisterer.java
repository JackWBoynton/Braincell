package net.bottomtextdanny.braincell.mod._base.registry.block_extensions;

import net.bottomtextdanny.braincell.mod._base.registry.managing.DeferrorType;
import net.bottomtextdanny.braincell.mod._base.registry.managing.ModDeferringManager;
import net.bottomtextdanny.braincell.mod._base.registry.managing.BCRegistry;
import net.bottomtextdanny.braincell.mod.world.builtin_blocks.BCChestBlock;
import net.minecraft.world.level.block.Block;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public interface ExtraChestRegisterer<T extends BCChestBlock, U extends BCChestBlock> extends ExtraBlockRegisterer<T> {

    @Override
    default void registerExtra(T object, ModDeferringManager solving) {
        Objects.requireNonNull(object);
        Optional<BCRegistry<Block>> registryOp = solving.getRegistryDeferror(DeferrorType.BLOCK);
        registryOp.ifPresent(registry -> {
            U newTrappedChestBlock = trappedFactory(object).get();
            newTrappedChestBlock.setRegistryName(solving.getModID(), "trapped_" + object.getRegistryName().getPath());
            solving.doHooksForObject(DeferrorType.BLOCK, newTrappedChestBlock);
            registry.addDeferredRegistry(() -> newTrappedChestBlock);
            setTrapped(newTrappedChestBlock);
        });
    }

    void setTrapped(U declaration);

    Supplier<U> trappedFactory(BCChestBlock base);

    U trapped();
}
