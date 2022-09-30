/*
 * Copyright Sunday August 07 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.tables;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.accessory.AccessoryAttributeModifier;
import bottomtextdanny.braincell.libraries.accessory.AttributeModifierAccessory;
import bottomtextdanny.braincell.libraries.registry.BCRegistry;
import bottomtextdanny.braincell.libraries.registry.RegistryHelper;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.UUID;
import java.util.function.Supplier;

public final class BCAccessoryAttributeModifiers {
	public static final ResourceKey<Registry<AccessoryAttributeModifier>> REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Braincell.ID, "accessory_attribute_modifier"));
	private static final DeferredRegister<AccessoryAttributeModifier> DEFERRED_REGISTERER = DeferredRegister.create(REGISTRY_KEY, Braincell.ID);
	public final static Supplier<IForgeRegistry<AccessoryAttributeModifier>> REGISTRY = DEFERRED_REGISTERER.makeRegistry(() -> new RegistryBuilder<>());
	public static final BCRegistry<AccessoryAttributeModifier> ENTRIES = new BCRegistry<>();
	public static final RegistryHelper<AccessoryAttributeModifier> HELPER = new RegistryHelper<>(Braincell.DEFERRING_STATE, ENTRIES);

	public static final Wrap<AccessoryAttributeModifier> MAX_HEALTH =
		HELPER.defer("max_health", () -> new AccessoryAttributeModifier(Attributes.MAX_HEALTH,
			UUID.fromString("1cc7f086-5151-11ec-bf63-0242ac130002"),
			UUID.fromString("0951da4f-7cf6-44ef-98f5-db209ee346db"),
			UUID.fromString("469cf3c1-12bf-41fb-8759-5233bad78e80")
			));

	public static final Wrap<AccessoryAttributeModifier> KNOCKBACK_RESISTANCE =
		HELPER.defer("knockback_resistance", () -> new AccessoryAttributeModifier(Attributes.KNOCKBACK_RESISTANCE,
			UUID.fromString("d687eba7-9b9d-49fc-9828-bb35e654521c"),
			UUID.fromString("5adcbd09-1614-4527-8981-cba9f37e7c0e"),
			UUID.fromString("d48458a9-b59b-43b2-99c7-f160c0e83092")
		));

	public static final Wrap<AccessoryAttributeModifier> MOVEMENT_SPEED =
		HELPER.defer("movement_speed", () -> new AccessoryAttributeModifier(Attributes.MOVEMENT_SPEED,
			UUID.fromString("b13dc7d3-3396-4d6c-a7dd-0cd4372f77ff"),
			UUID.fromString("6c53e76a-768d-420d-a26b-f8286a978cc5"),
			UUID.fromString("ade09de1-5a28-43b9-8de5-794bf9cf8187")
		));

	public static final Wrap<AccessoryAttributeModifier> ATTACK_DAMAGE =
		HELPER.defer("attack_damage", () -> new AccessoryAttributeModifier(Attributes.ATTACK_DAMAGE,
			UUID.fromString("6dc5a831-2491-4732-8a75-2639c319e0c1"),
			UUID.fromString("d0722dce-1167-44a8-bfca-2b564dfc4613"),
			UUID.fromString("8518fe35-9f2d-4d51-b79f-b8b96214a77e")
		));

	public static final Wrap<AccessoryAttributeModifier> ATTACK_KNOCKBACK =
		HELPER.defer("attack_knockback", () -> new AccessoryAttributeModifier(Attributes.ATTACK_KNOCKBACK,
			UUID.fromString("6a7c0ecf-be31-460f-b6c0-acfe087eb02b"),
			UUID.fromString("206c4ac0-76fa-479b-832b-688bcf45b579"),
			UUID.fromString("87a3d66f-5a80-4224-893a-e29af9a2d07f")
		));

	public static final Wrap<AccessoryAttributeModifier> ATTACK_SPEED =
		HELPER.defer("attack_speed", () -> new AccessoryAttributeModifier(Attributes.ATTACK_SPEED,
			UUID.fromString("d5047e8a-a0f7-45a4-ac2e-6e2de582a0e0"),
			UUID.fromString("5675317a-380b-4bd6-8d68-07f331ef9c66"),
			UUID.fromString("02bbff91-ca02-4eb5-b0ae-7b5cad7eee1a")
		));

	public static final Wrap<AccessoryAttributeModifier> ARMOR =
		HELPER.defer("armor", () -> new AccessoryAttributeModifier(Attributes.ARMOR,
			UUID.fromString("86516717-7261-4cf2-a794-fad5c7df9095"),
			UUID.fromString("94d03c52-a284-4aa3-a12c-9600f77c9ad4"),
			UUID.fromString("540b424f-86d5-4724-b8d6-c8ecb6fe5f82")
		));

	public static final Wrap<AccessoryAttributeModifier> LUCK =
		HELPER.defer("luck", () -> new AccessoryAttributeModifier(Attributes.LUCK,
			UUID.fromString("eaf3db15-e710-4e75-bc1d-9f396d8b13f5"),
			UUID.fromString("a62c17bd-7881-45a5-9235-777ea00632e1"),
			UUID.fromString("7c9db08e-5668-455a-af66-067c22242e08")
		));

	public static final Wrap<AccessoryAttributeModifier> CRITICAL_DAMAGE =
		HELPER.defer("critical_damage", () -> new AccessoryAttributeModifier(BCAttributes.CRITICAL_DAMAGE.get(),
			UUID.fromString("eaf3db15-e710-4e75-bc1d-9f396d8b13f5"),
			UUID.fromString("a62c17bd-7881-45a5-9235-777ea00632e1"),
			UUID.fromString("7c9db08e-5668-455a-af66-067c22242e08")
		));

	public static final Wrap<AccessoryAttributeModifier> CRITICAL_CHANCE =
		HELPER.defer("critical_chance", () -> new AccessoryAttributeModifier(BCAttributes.CRITICAL_CHANCE.get(),
			UUID.fromString("eaf3db15-e710-4e75-bc1d-9f396d8b13f5"),
			UUID.fromString("a62c17bd-7881-45a5-9235-777ea00632e1"),
			UUID.fromString("7c9db08e-5668-455a-af66-067c22242e08")
		));

	public static final Wrap<AccessoryAttributeModifier> ARCHERY_DAMAGE_MULTIPLIER =
		HELPER.defer("archery_damage_multiplier", () -> new AccessoryAttributeModifier(BCAttributes.ARCHERY_DAMAGE_MULTIPLIER.get(),
			UUID.fromString("eaf3db15-e710-4e75-bc1d-9f396d8b13f5"),
			UUID.fromString("a62c17bd-7881-45a5-9235-777ea00632e1"),
			UUID.fromString("7c9db08e-5668-455a-af66-067c22242e08")
		));

	public static final Wrap<AccessoryAttributeModifier> ARROW_SPEED_MULTIPLIER =
		HELPER.defer("arrow_speed_multiplier", () -> new AccessoryAttributeModifier(BCAttributes.ARROW_SPEED_MULTIPLIER.get(),
			UUID.fromString("eaf3db15-e710-4e75-bc1d-9f396d8b13f5"),
			UUID.fromString("a62c17bd-7881-45a5-9235-777ea00632e1"),
			UUID.fromString("7c9db08e-5668-455a-af66-067c22242e08")
		));

	public static void registerRegistry() {
		DEFERRED_REGISTERER.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
