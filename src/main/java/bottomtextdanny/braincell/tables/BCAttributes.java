/*
 * Copyright Sunday August 07 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.tables;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.accessory.AccessoryAttributeModifier;
import bottomtextdanny.braincell.libraries.registry.BCRegistry;
import bottomtextdanny.braincell.libraries.registry.RegistryHelper;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.UUID;
import java.util.function.Supplier;

public final class BCAttributes {
	public static final BCRegistry<Attribute> ENTRIES = new BCRegistry<>();
	public static final RegistryHelper<Attribute> HELPER = new RegistryHelper<>(Braincell.DEFERRING_STATE, ENTRIES);

	public static final Wrap<Attribute> CRITICAL_DAMAGE =
		HELPER.defer("generic.critical_damage", () -> new RangedAttribute(
			"attribute.name.generic.critical_damage", 1.0D, -1024.0D, 1024.0D
		));

	public static final Wrap<Attribute> CRITICAL_CHANCE =
		HELPER.defer("generic.critical_chance", () -> new RangedAttribute(
			"attribute.name.generic.critical_chance", 0.0D, 0.0D, 1024.0D
		));

	public static final Wrap<Attribute> ARCHERY_DAMAGE_MULTIPLIER =
		HELPER.defer("generic.archery_damage_multiplier", () -> new RangedAttribute(
			"attribute.name.generic.archery_damage_multiplier", 1.0D, 0.0D, 10.24D
		));

	public static final Wrap<Attribute> ARROW_SPEED_MULTIPLIER =
		HELPER.defer("generic.arrow_speed_multiplier", () -> new RangedAttribute(
			"attribute.name.generic.arrow_speed_multiplier", 1.0D, 0.0D, 10.24D
		));
}
