/*
 * Copyright Sunday August 07 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.tables;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries._minor.entity.EntityKey;
import bottomtextdanny.braincell.libraries.accessory.AccessoryFactory;
import bottomtextdanny.braincell.libraries.accessory.AccessoryKey;
import bottomtextdanny.braincell.libraries.accessory.StackAccessory;
import bottomtextdanny.braincell.libraries.entity_animation.AnimationGetter;
import bottomtextdanny.braincell.libraries.entity_variant.IndexedFormManager;
import bottomtextdanny.braincell.libraries.entity_variant.StringedFormManager;
import bottomtextdanny.braincell.libraries.entity_variant.VariantProvider;
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

import java.util.List;
import java.util.function.Supplier;

public final class BCEntityKeys {
	public static final ResourceKey<Registry<EntityKey<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Braincell.ID, "entity_key"));
	private static final DeferredRegister<EntityKey<?>> DEFERRED_REGISTERER = DeferredRegister.create(REGISTRY_KEY, Braincell.ID);
	public final static Supplier<IForgeRegistry<EntityKey<?>>> REGISTRY = DEFERRED_REGISTERER.makeRegistry(() -> new RegistryBuilder<>());
	public static final BCRegistry<EntityKey<?>> ENTRIES = new BCRegistry<>();
	public static final RegistryHelper<EntityKey<?>> HELPER = new RegistryHelper<>(Braincell.DEFERRING_STATE, ENTRIES);

	public static final Wrap<EntityKey<AnimationGetter>> ANIMATIONS = HELPER.defer("animations", () -> new EntityKey<>(AnimationGetter.class, null));
	public static final Wrap<EntityKey<IndexedFormManager>> INDEXED_FORMS = HELPER.defer("indexed_forms", () -> new EntityKey<>(IndexedFormManager.class, null));
	public static final Wrap<EntityKey<StringedFormManager>> STRINGED_FORMS = HELPER.defer("stringed_forms", () -> new EntityKey<>(StringedFormManager.class, null));

	public static void registerRegistry() {
		DEFERRED_REGISTERER.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
