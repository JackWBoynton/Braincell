package bottomtextdanny.braincell.mod.entity.modules.animatable;

import bottomtextdanny.braincell.mod.network.stc.MSGAnimatedEntityDeathEnd;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;

public interface LivingAnimatableProvider extends BaseAnimatableProvider {
   default void onDeathAnimationEnd() {
      Entity asEntity = (Entity)this;
      (new MSGAnimatedEntityDeathEnd(asEntity.getId())).sendTo(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> {
         return asEntity;
      }));
   }

   @OnlyIn(Dist.CLIENT)
   default void onDeathAnimationEndClient() {
      LivingEntity entity = (LivingEntity)this;

      for(int i = 0; i < 20; ++i) {
         double d0 = entity.getRandom().nextGaussian() * 0.02;
         double d1 = entity.getRandom().nextGaussian() * 0.02;
         double d2 = entity.getRandom().nextGaussian() * 0.02;
         entity.level.addParticle(ParticleTypes.POOF, entity.getRandomX(1.0), entity.getRandomY(), entity.getRandomZ(1.0), d0, d1, d2);
      }

   }

   default void onDeathAnimationStart() {
   }

   default AnimationHandler getLocalAnimationHandler() {
      if (!this.operateAnimatableModule()) {
         throw new UnsupportedOperationException("Tried to call bootstrap handler on deactivated AnimatableModule, entity:" + ((Entity)this).getType().getRegistryName().toString());
      } else {
         return ((LivingAnimatableModule)this.animatableModule()).getLocalHandler();
      }
   }

   @Nullable
   default Animation getDeathAnimation() {
      return null;
   }
}
