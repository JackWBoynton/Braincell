package bottomtextdanny.braincell.mod.world.entity_utilities;

import bottomtextdanny.braincell.mod.entity.modules.animatable.LivingAnimatableProvider;
import net.minecraft.world.damagesource.DamageSource;

public interface EntityHurtAnimation extends EntityHurtCallout, LivingAnimatableProvider {
   default float hurtCallOut(float damage, DamageSource source) {
      if (this.canPlayHurtAnimation(damage, source)) {
         this.playHurtAnimation(damage, source);
      }

      return damage;
   }

   default boolean canPlayHurtAnimation(float damage, DamageSource source) {
      return true;
   }

   void playHurtAnimation(float var1, DamageSource var2);
}
