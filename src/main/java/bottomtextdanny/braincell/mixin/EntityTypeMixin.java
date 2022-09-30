/*
 * Copyright Saturday September 03 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.mixin;

import bottomtextdanny.braincell.libraries._minor.entity.EntityKey;
import bottomtextdanny.braincell.mixin_support.EntityTypeExtensor;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;

import java.util.IdentityHashMap;
import java.util.Map;

@Mixin(EntityType.class)
public class EntityTypeMixin implements EntityTypeExtensor {
	private final Map<EntityKey<?>, Object> map = new IdentityHashMap<>();

	@Override
	public <T> T addAttribute(EntityKey<T> key, T attribute) {
		return (T)map.put(key, attribute);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAttributeOrDefault(EntityKey<T> key) {
		return (T)map.getOrDefault(key, key.defaultValue());
	}

	@Override
	public <T> T getAttribute(EntityKey<T> key) {
		Object o = map.get(key);
		return key.clazz().isAssignableFrom(o.getClass()) ? (T) o : null;
	}

	@Override
	public boolean hasAttribute(EntityKey<?> key) {
		return map.containsKey(key);
	}
}
