package bottomtextdanny.braincell.libraries.registry.blocks;

import bottomtextdanny.braincell.libraries.registry.block_extensions.ExtraBlockRegisterer;
import bottomtextdanny.braincell.libraries.registry.ModDeferringManager;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;

public class BCFenceBlock extends FenceBlock implements ExtraBlockRegisterer<BCFenceBlock> {
    private FenceGateBlock gate;

    public BCFenceBlock(Properties properties) {
        super(properties);
    }

    public FenceGateBlock gate() {
        return this.gate;
    }

    @Override
    public void registerExtra(BCFenceBlock base, ResourceLocation loc, ModDeferringManager solving) {
        solving.getRegistryDeferror(Registry.BLOCK_REGISTRY).ifPresent(registry -> {
            this.gate = new FenceGateBlock(Properties.copy(this));
            ResourceLocation key = new ResourceLocation(solving.getModID(), loc.getPath() + "_gate");
            solving.doHooksForObject(Registry.BLOCK, key, this.gate);
            registry.addDeferredRegistry(key, () -> this.gate);
        });
    }
}
