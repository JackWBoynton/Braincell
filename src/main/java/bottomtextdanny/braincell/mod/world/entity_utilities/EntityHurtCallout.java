package bottomtextdanny.braincell.mod.world.entity_utilities;

import net.minecraft.world.damagesource.DamageSource;

public interface EntityHurtCallout {
	
	float hurtCallOut(float damage, DamageSource source);
}
