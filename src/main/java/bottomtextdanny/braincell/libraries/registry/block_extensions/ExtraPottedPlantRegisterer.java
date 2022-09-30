package bottomtextdanny.braincell.libraries.registry.block_extensions;

import bottomtextdanny.braincell.libraries.registry.BCRegistry;
import bottomtextdanny.braincell.libraries.registry.ModDeferringManager;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public interface ExtraPottedPlantRegisterer<T extends Block, U extends FlowerPotBlock> extends ExtraBlockRegisterer<T> {

	@Override
	default void registerExtra(T object, ResourceLocation key, ModDeferringManager solving) {
		Objects.requireNonNull(object);
		Optional<BCRegistry<Block>> registryOp = solving.getRegistryDeferror(Registry.BLOCK_REGISTRY);
		registryOp.ifPresent(registry -> {
			U newPotBlock = pottedFactory(object).get();

			ResourceLocation location = new ResourceLocation(solving.getModID(), "potted_" + key.getPath());

			solving.doHooksForObject(Registry.BLOCK, location, newPotBlock);
			registry.addDeferredRegistry(location, () -> newPotBlock);
			decPotted(newPotBlock);
		});
	}
    
    void decPotted(U value);

    default Supplier<U> pottedFactory(T base) {
    	return () -> (U) new FlowerPotBlock(base, BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion());
    }
	
	@Override
	default boolean executeSideRegistry() {
		return true;
	}
	
	U potted();
}
