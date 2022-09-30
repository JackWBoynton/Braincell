package bottomtextdanny.braincell.libraries._minor.entity;

import bottomtextdanny.braincell.libraries.entity_animation.LivingAnimatableProvider;
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
