/*
 * Copyright Sunday August 07 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.tables;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.accessory.AccessoryFactory;
import bottomtextdanny.braincell.libraries.accessory.AccessoryKey;
import bottomtextdanny.braincell.libraries.accessory.IAccessory;
import bottomtextdanny.braincell.libraries.accessory.StackAccessory;
import bottomtextdanny.braincell.libraries.chart.segment.Chart;
import bottomtextdanny.braincell.libraries.registry.BCRegistry;
import bottomtextdanny.braincell.libraries.registry.RegistryHelper;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public final class BCAccessoryKeys {
	public static final ResourceKey<Registry<AccessoryKey<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Braincell.ID, "accessory_key"));
	private static final DeferredRegister<AccessoryKey<?>> DEFERRED_REGISTERER = DeferredRegister.create(REGISTRY_KEY, Braincell.ID);
	public final static Supplier<IForgeRegistry<AccessoryKey<?>>> REGISTRY = DEFERRED_REGISTERER.makeRegistry(() -> new RegistryBuilder<>());
	public static final BCRegistry<AccessoryKey<?>> ENTRIES = new BCRegistry<>();
	public static final RegistryHelper<AccessoryKey<?>> HELPER = new RegistryHelper<>(Braincell.DEFERRING_STATE, ENTRIES);

	public static final Wrap<AccessoryKey<IAccessory>> EMPTY = create("empty", (player, factory) -> new IAccessory.EmptyAccessory());
	public static final Wrap<AccessoryKey<StackAccessory>> STACK_EMPTY = create("stack_empty", (player, factory) -> StackAccessory.EMPTY);

	public static <E extends IAccessory> Wrap<AccessoryKey<E>> create(String name, AccessoryFactory<E> factory) {
		return HELPER.defer(name, () -> AccessoryKey.createKey(new ResourceLocation(Braincell.ID, name), factory));
	}

	public static void registerRegistry() {
		DEFERRED_REGISTERER.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
