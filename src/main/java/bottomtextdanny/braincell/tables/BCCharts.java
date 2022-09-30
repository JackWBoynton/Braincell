/*
 * Copyright Sunday August 07 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.tables;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.chart.segment.Chart;
import bottomtextdanny.braincell.libraries.registry.BCRegistry;
import bottomtextdanny.braincell.libraries.registry.RegistryHelper;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.List;
import java.util.function.Supplier;

public final class BCCharts {
	public static final ResourceKey<Registry<Chart>> REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Braincell.ID, "chart"));
	private static final DeferredRegister<Chart> DEFERRED_REGISTERER = DeferredRegister.create(REGISTRY_KEY, Braincell.ID);
	public final static Supplier<IForgeRegistry<Chart>> REGISTRY = DEFERRED_REGISTERER.makeRegistry(() -> new RegistryBuilder<>());
	public static final BCRegistry<Chart> ENTRIES = new BCRegistry<>();
	public static final RegistryHelper<Chart> HELPER = new RegistryHelper<>(Braincell.DEFERRING_STATE, ENTRIES);
	
	public static void registerRegistry() {
		DEFERRED_REGISTERER.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
