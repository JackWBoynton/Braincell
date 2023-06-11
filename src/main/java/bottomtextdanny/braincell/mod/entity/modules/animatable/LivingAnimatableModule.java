package bottomtextdanny.braincell.mod.entity.modules.animatable;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity.RemovalReason;

public class LivingAnimatableModule extends BaseAnimatableModule {
   private static final BCAnimationToken TOKEN = new BCAnimationToken();
   private final LivingEntity living;
   private final AnimationHandler localHandler;
   private boolean deathHasBegun;

   public LivingAnimatableModule(LivingEntity entity, AnimationGetter manager) {
      super(entity, manager);
      this.localHandler = new AnimationHandler(entity);
      this.localHandler.setIndex(0, TOKEN);
      this.animationHandlerList().add(this.localHandler);
      this.living = entity;
   }

   public void tick() {
      super.tick();
      if (this.living.getHealth() <= 0.0F) {
         Animation deathAnimation = ((LivingAnimatableProvider)this.provider).getDeathAnimation();
         if (!this.deathHasBegun) {
            if (deathAnimation != null && !this.living.level.isClientSide) {
               this.localHandler.play(deathAnimation);
            }

            this.deathHasBegun = true;
            ((LivingAnimatableProvider)this.provider).onDeathAnimationStart();
         } else if (deathAnimation != null && this.localHandler.isPlaying(deathAnimation)) {
            this.tickDeathHook(deathAnimation.getDuration());
         } else {
            this.tickDeathHook(20);
         }
      }

   }

   public AnimationHandler getLocalHandler() {
      return this.localHandler;
   }

   public void tickDeathHook(int time) {
      ++this.living.deathTime;
      if (!this.living.level.isClientSide && this.living.deathTime >= time) {
         ((LivingAnimatableProvider)this.provider).onDeathAnimationEnd();
         this.living.remove(RemovalReason.KILLED);
      }

   }

   public boolean deathHasBegun() {
      return this.deathHasBegun;
   }
}
