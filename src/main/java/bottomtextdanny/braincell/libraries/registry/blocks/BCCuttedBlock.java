package bottomtextdanny.braincell.libraries.registry.blocks;

import bottomtextdanny.braincell.libraries.registry.block_extensions.ExtraBlockRegisterer;
import bottomtextdanny.braincell.libraries.registry.ModDeferringManager;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;

import javax.annotation.Nullable;

public class BCCuttedBlock extends Block implements ExtraBlockRegisterer<Block> {
    @Nullable
	private String mask;
    private StairBlock stairs;
    private SlabBlock slab;

    public BCCuttedBlock(Properties properties, String mask) {
        super(properties);
        this.mask = mask;
    }
	
	public BCCuttedBlock(Properties properties) {
		super(properties);
		this.mask = null;
	}

    public SlabBlock slab() {
        return this.slab;
    }

    public StairBlock stairs() {
        return this.stairs;
    }

    @Override
    public void registerExtra(Block object, ResourceLocation key, ModDeferringManager solving) {
        solving.getRegistryDeferror(Registry.BLOCK_REGISTRY).ifPresent(registry -> {
            if (this.mask == null) this.mask = key.getPath();
            this.stairs = new StairBlock(object.defaultBlockState(), Properties.copy(object));
            ResourceLocation location = new ResourceLocation(solving.getModID(), this.mask + "_stairs");
            solving.doHooksForObject(Registry.BLOCK, location, this.stairs);
            registry.addDeferredRegistry(location, () -> this.stairs);
            this.slab = new SlabBlock(Properties.copy(object));
            location = new ResourceLocation(solving.getModID(), this.mask + "_slab");
            solving.doHooksForObject(Registry.BLOCK, location, this.slab);
            registry.addDeferredRegistry(location, () -> this.slab);
            this.mask = null;
        });
    }
}
