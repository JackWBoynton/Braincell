package bottomtextdanny.braincell.libraries.registry.blocks;

import bottomtextdanny.braincell.libraries.registry.block_extensions.ExtraBlockRegisterer;
import bottomtextdanny.braincell.libraries.registry.ModDeferringManager;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;

import java.util.function.Supplier;

public class BCPlanksBlock extends Block implements ExtraBlockRegisterer<Block> {
	private final Supplier<? extends BlockItem> chestItem;
    private StairBlock stairs;
    private SlabBlock slab;

    public BCPlanksBlock(Properties properties, Supplier<? extends BlockItem> chest) {
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
    public void registerExtra(Block base, ResourceLocation key, ModDeferringManager solving) {
        solving.getRegistryDeferror(Registry.BLOCK_REGISTRY).ifPresent(registry -> {
            String path = key.getPath();
            ResourceLocation location = new ResourceLocation(solving.getModID(), path.substring(path.lastIndexOf('_')) + "_stairs");
            this.stairs = new StairBlock(base.defaultBlockState(), Properties.copy(base));
            solving.doHooksForObject(Registry.BLOCK, location, this.stairs);
            registry.addDeferredRegistry(location, () -> this.stairs);
            this.slab = new SlabBlock(Properties.copy(base));
            location = new ResourceLocation(solving.getModID(), path.substring(path.lastIndexOf('_')) + "_slab");
            solving.doHooksForObject(Registry.BLOCK, location, this.slab);
            registry.addDeferredRegistry(location, () -> this.slab);
        });
    }
}
