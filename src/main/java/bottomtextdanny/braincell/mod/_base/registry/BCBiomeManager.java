package bottomtextdanny.braincell.mod._base.registry;

import bottomtextdanny.braincell.mod._base.registry.managing.Wrap;
import bottomtextdanny.braincell.Braincell;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public final class BCBiomeManager extends Wrap<Biome> {
	private BiomeDictionary.Type[] catArray;
	private ResourceKey<Biome> biomeKey;
	
	private BCBiomeManager(ResourceLocation name, Supplier<Biome> sup, BiomeDictionary.Type[] categories) {
		super(name, sup);
		this.catArray = categories;
	}
	
	public void inferBiomeKey(ResourceKey<Biome> biomeKey) {
		if (this.biomeKey == null) this.biomeKey = biomeKey;
	}
	
	@Override
	public void solve() {
		super.solve();
		ResourceKey<Biome> biomeKey = ResourceKey.create(ForgeRegistries.Keys.BIOMES, this.key);
		inferBiomeKey(biomeKey);
		FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLCommonSetupEvent event) -> {
			ResourceKey<Biome> biomeKey1 = ResourceKey.create(ForgeRegistries.Keys.BIOMES, ForgeRegistries.BIOMES.getKey(this.obj));
			inferBiomeKey(biomeKey1);
			this.obj = ForgeRegistries.BIOMES.getValue(biomeKey1.location());
		});
		BiomeDictionary.addTypes(biomeKey, this.catArray);
        this.catArray = null;
	}
	
	public ResourceKey<Biome> getBiomeKey() {
		return this.biomeKey;
	}
	
	public static class Builder {
		private BiomeDictionary.Type[] catArray;
		private final Supplier<Biome> sup;
		private final ResourceLocation key;
		
		private Builder(ResourceLocation key, Supplier<Biome> sup) {
			this.key = key;
			this.sup = sup;
		}
		
		public static Builder start(String name, Supplier<Biome> sup) {
			return new Builder(new ResourceLocation(Braincell.ID, name), sup);
		}
		
		public Builder putTypes(BiomeDictionary.Type... types) {
            this.catArray = types;
			return this;
		}
		
		public BCBiomeManager build() {
			BCBiomeManager reg = new BCBiomeManager(this.key, this.sup, this.catArray);
			return reg;
		}
	}
}
