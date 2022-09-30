/*
 * Copyright Saturday September 03 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.registry.entity_type_helper;

import bottomtextdanny.braincell.libraries._minor.entity.EntityParams;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public record EntityParameterDeferror(EntityWrap<? extends EntityType<?>> type, List<EntityBuilder.Param<?>> parameters) {

	void put() {
		EntityType<?> type = this.type.get();

		for (EntityBuilder.Param<?> parameter : parameters) {
			parameter.append(type);
		}
	}
}
