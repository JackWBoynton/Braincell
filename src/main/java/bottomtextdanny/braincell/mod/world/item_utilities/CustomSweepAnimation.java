package bottomtextdanny.braincell.mod.world.item_utilities;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public interface CustomSweepAnimation {
   default void sweepParticleHook(Player player) {
      double d0 = (double)(-Mth.sin(player.getYRot() * 0.017453292F));
      double d1 = (double)Mth.cos(player.getYRot() * 0.017453292F);
      if (player.level instanceof ServerLevel) {
         ((ServerLevel)player.level).sendParticles(this.getSweepParticle(player), player.getX() + d0 * 1.3, player.getY(0.5), player.getZ() + d1 * 1.3, 0, d0, 0.0, d1, 0.0);
      }

   }

   default ParticleOptions getSweepParticle(Player player) {
      return ParticleTypes.SWEEP_ATTACK;
   }
}
