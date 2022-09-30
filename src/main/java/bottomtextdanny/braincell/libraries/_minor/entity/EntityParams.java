/*
 * Copyright Saturday September 03 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries._minor.entity;

import bottomtextdanny.braincell.mixin_support.EntityTypeExtensor;
import net.minecraft.world.entity.EntityType;

public final class EntityParams {

	private EntityParams() {}

	public static <T> T add(EntityType<?> type, EntityKey<T> key, T attribute) {
		return ((EntityTypeExtensor)type).addAttribute(key, attribute);
	}

	public static <T> T getOrDefault(EntityType<?> type, EntityKey<T> key) {
		return ((EntityTypeExtensor)type).getAttributeOrDefault(key);
	}

	public static <T> T get(EntityType<?> type, EntityKey<T> key) {
		return ((EntityTypeExtensor)type).getAttribute(key);
	}

	public static boolean has(EntityType<?> type, EntityKey<?> key) {
		return ((EntityTypeExtensor)type).hasAttribute(key);
	}
}
