/*
 * Copyright Saturday September 03 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.mixin_support;

import bottomtextdanny.braincell.libraries._minor.entity.EntityKey;

public interface EntityTypeExtensor {

	<T> T addAttribute(EntityKey<T> key, T attribute);

	@SuppressWarnings("unchecked")
	<T> T getAttributeOrDefault(EntityKey<T> key);

	<T> T getAttribute(EntityKey<T> key);

	boolean hasAttribute(EntityKey<?> key);
}
