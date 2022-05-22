package bottomtextdanny.braincell.mod.world.builtin_blocks;

import bottomtextdanny.braincell.mod._base.registry.block_extensions.ExtraBlockRegisterer;
import bottomtextdanny.braincell.mod._base.registry.managing.DeferrorType;
import bottomtextdanny.braincell.mod._base.registry.managing.ModDeferringManager;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;

import java.util.function.Supplier;

public class DEPlanksBlock extends Block implements ExtraBlockRegisterer<Block> {
	private final Supplier<? extends BlockItem> chestItem;
    private StairBlock stairs;
    private SlabBlock slab;

    public DEPlanksBlock(Properties properties, Supplier<? extends BlockItem> chest) {
        super(properties);
        this.chestItem = chest;
    }

    public SlabBlock slab() {
        return this.slab;
    }

    public StairBlock stairs() {
        return this.stairs;
    }

    @Override
    public void registerExtra(Block base, ModDeferringManager solving) {
        solving.getRegistryDeferror(DeferrorType.BLOCK).ifPresent(registry -> {
            String baseName = getRegistryName().getPath().substring(0, getRegistryName().getPath().lastIndexOf('_'));
            this.stairs = new StairBlock(base.defaultBlockState(), Properties.copy(base));
            this.stairs.setRegistryName(baseName + "_stairs");
            solving.doHooksForObject(DeferrorType.BLOCK, this.stairs);
            registry.addDeferredRegistry(() -> this.stairs);
            this.slab = new SlabBlock(Properties.copy(base));
            this.slab.setRegistryName(baseName + "_slab");
            solving.doHooksForObject(DeferrorType.BLOCK, this.slab);
            registry.addDeferredRegistry(() -> this.slab);
        });
    }
}
