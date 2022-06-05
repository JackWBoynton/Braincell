package bottomtextdanny.braincell.mod.entity.modules.animatable;

import bottomtextdanny.braincell.mod.network.stc.MSGAnimatedEntityDeathEnd;
import net.minecraft.core.particles.ParticleTypes;
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
        for(int i = 0; i < 20; ++i) {
            double d0 = entity.getRandom().nextGaussian() * 0.02D;
            double d1 = entity.getRandom().nextGaussian() * 0.02D;
            double d2 = entity.getRandom().nextGaussian() * 0.02D;
            entity.level.addParticle(ParticleTypes.POOF, entity.getRandomX(1.0D), entity.getRandomY(), entity.getRandomZ(1.0D), d0, d1, d2);
        }
    }

    default void onDeathAnimationStart() {}

    default AnimationHandler<?> getLocalAnimationHandler() {
        if (!operateAnimatableModule()) throw new UnsupportedOperationException("Tried to call bootstrap handler on deactivated AnimatableModule, entity:" + ((Entity)this).getType().getRegistryName().toString());
        return animatableModule().getLocalHandler();
    }

    @Nullable
    default Animation getDeathAnimation() {return null;}
}
