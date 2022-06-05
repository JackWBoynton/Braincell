package bottomtextdanny.braincell.mod.entity.modules.animatable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class LivingAnimatableModule extends BaseAnimatableModule<LivingAnimatableProvider> {
    private static final BCAnimationToken TOKEN = new BCAnimationToken();
    private final LivingEntity living;
    private final AnimationHandler<?> localHandler;
    private boolean deathHasBegun;

    public LivingAnimatableModule(LivingEntity entity, AnimationGetter manager) {
        super(entity, manager);
        this.localHandler = new AnimationHandler<>(entity);
        this.localHandler.setIndex(0, TOKEN);
        animationHandlerList().add(this.localHandler);
        this.living = entity;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.living.getHealth() <= 0.0F) {
            Animation<?> deathAnimation = this.provider.getDeathAnimation();
            if (!this.deathHasBegun) {
                if (deathAnimation != null) {
                    if (!living.level.isClientSide) this.localHandler.play(deathAnimation);
                }
                this.deathHasBegun = true;
                this.provider.onDeathAnimationStart();
            } else if (deathAnimation != null && localHandler.isPlaying(deathAnimation)) {
                tickDeathHook(deathAnimation.getDuration());
            } else {
                tickDeathHook(20);
            }
        }
    }

    public AnimationHandler<?> getLocalHandler() {
        return this.localHandler;
    }

    public void tickDeathHook(int time) {
        ++this.living.deathTime;

        if (!this.living.level.isClientSide && this.living.deathTime >= time) {
            this.provider.onDeathAnimationEnd();
            this.living.remove(Entity.RemovalReason.KILLED);
        }
    }

    public boolean deathHasBegun() {
        return this.deathHasBegun;
    }
}
