package net.bottomtextdanny.braincell.mod.world.entity_utilities;

import net.bottomtextdanny.braincell.mod._base.entity.modules.animatable.LivingAnimatableProvider;
import net.minecraft.world.damagesource.DamageSource;

public interface EntityHurtAnimation extends EntityHurtCallout, LivingAnimatableProvider {
	
	@Override
	default float hurtCallOut(float damage, DamageSource source) {
		if (canPlayHurtAnimation(damage,source))
			playHurtAnimation(damage,source);
		return damage;
	}
	
	default boolean canPlayHurtAnimation(float damage, DamageSource source) {
		return true;
	}
	
	void playHurtAnimation(float damage, DamageSource source);
}
