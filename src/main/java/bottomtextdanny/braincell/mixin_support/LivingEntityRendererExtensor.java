/*
 * Copyright Saturday August 27 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.mixin_support;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.world.entity.LivingEntity;

public interface LivingEntityRendererExtensor<T extends LivingEntity, M extends EntityModel<T>> {

	M getModel();
}
