package bottomtextdanny.braincell.libraries.entity_animation;

import bottomtextdanny.braincell.libraries.entity_animation.animations.Animation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

public class LivingAnimatableModule extends BaseAnimatableModule {
    private static final BCAnimationToken TOKEN = new BCAnimationToken();
    private final AnimationHandler localHandler;
    private boolean deathHasBegun;

    public LivingAnimatableModule(EntityType<?> entityType) {
        super(entityType);
        this.localHandler = new AnimationHandler();
        this.localHandler.setIndex(0, TOKEN);
        animationHandlerList().add(this.localHandler);
    }

    @Override
    public <E extends Entity & BaseAnimatableProvider<?>> void tick(E entity) {
        super.tick(entity);
        if (entity instanceof LivingEntity living) {

            if (living.getHealth() <= 0.0F) {
                LivingAnimatableProvider provider = (LivingAnimatableProvider) living;
                Animation<?> deathAnimation = provider.getDeathAnimation();

                if (!this.deathHasBegun) {
                    if (deathAnimation != null) {
                        if (!living.level.isClientSide) this.localHandler.play(living, deathAnimation);
                    }
                    this.deathHasBegun = true;
                    provider.onDeathAnimationStart();
                } else if (deathAnimation != null && localHandler.isPlaying(deathAnimation)) {
                    tickDeathHook(living, deathAnimation.getDuration());
                } else {
                    tickDeathHook(living, 20);
                }
            }
        }
    }

    public AnimationHandler getLocalHandler() {
        return this.localHandler;
    }

    public void tickDeathHook(LivingEntity entity, int time) {
        ++entity.deathTime;

        if (!entity.level.isClientSide && entity.deathTime >= time) {
            ((LivingAnimatableProvider)entity).onDeathAnimationEnd();
            entity.remove(Entity.RemovalReason.KILLED);
        }
    }

    public boolean deathHasBegun() {
        return this.deathHasBegun;
    }
}
