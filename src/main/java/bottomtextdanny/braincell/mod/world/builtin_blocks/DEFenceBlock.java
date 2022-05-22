package bottomtextdanny.braincell.mod.world.builtin_blocks;

import bottomtextdanny.braincell.mod._base.registry.block_extensions.ExtraBlockRegisterer;
import bottomtextdanny.braincell.mod._base.registry.managing.DeferrorType;
import bottomtextdanny.braincell.mod._base.registry.managing.ModDeferringManager;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;

public class DEFenceBlock extends FenceBlock implements ExtraBlockRegisterer<DEFenceBlock> {
    private FenceGateBlock gate;

    public DEFenceBlock(Properties properties) {
        super(properties);
    }

    public FenceGateBlock gate() {
        return this.gate;
    }

    @Override
    public void registerExtra(DEFenceBlock base, ModDeferringManager solving) {
        solving.getRegistryDeferror(DeferrorType.BLOCK).ifPresent(registry -> {
            this.gate = new FenceGateBlock(Properties.copy(this));
            this.gate.setRegistryName(getRegistryName().getPath() + "_gate");
            solving.doHooksForObject(DeferrorType.BLOCK, this.gate);
            registry.addDeferredRegistry(() -> this.gate);
        });
    }
}
