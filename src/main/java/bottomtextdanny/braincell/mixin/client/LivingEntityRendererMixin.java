/*
 * Copyright Saturday August 27 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.mixin.client;

import bottomtextdanny.braincell.mixin_support.LivingEntityRendererExtensor;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> implements LivingEntityRendererExtensor<T, M> {
	@Shadow protected M model;

	@Override
	public M getModel() {
		return model;
	}
}
