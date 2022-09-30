package bottomtextdanny.braincell.libraries.entity_animation;

import bottomtextdanny.braincell.libraries.entity_animation.animations.Animation;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;

public interface LivingAnimatableProvider extends BaseAnimatableProvider<LivingAnimatableModule> {

    default void onDeathAnimationEnd() {
        Entity asEntity = (Entity) this;
        new MSGAnimatedEntityDeathEnd(asEntity.getId())
                .sendTo(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> asEntity));
    }

    @OnlyIn(Dist.CLIENT)
    default void onDeathAnimationEndClient() {
        LivingEntity entity = (LivingEntity) this;
        RandomSource random = entity.getRandom();
        for(int i = 0; i < 20; ++i) {
            entity.level.addParticle(ParticleTypes.POOF, entity.getRandomX(1.0D), entity.getRandomY(), entity.getRandomZ(1.0D)
                , random.nextGaussian() * 0.02D, random.nextGaussian() * 0.02D, random.nextGaussian() * 0.02D);
        }
    }

    default void onDeathAnimationStart() {}

    default AnimationHandler getLocalAnimationHandler() {
        if (!operateAnimatableModule()) throw new UnsupportedOperationException("Tried to call internal handler with deactivated AnimatableModule, entity:" + ((Entity)this).getType().builtInRegistryHolder().key().toString());
        return animatableModule().getLocalHandler();
    }

    @Nullable
    default Animation getDeathAnimation() {return null;}
}
