package bottomtextdanny.braincell.mod.entity.modules.looped_walk;

import bottomtextdanny.braincell.base.BCMath;
import bottomtextdanny.braincell.mod.entity.modules.ModuleProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface LoopedWalkProvider extends ModuleProvider {
   LoopStepSoundPlayer LSP_DOUBLE = (entity, blockstate, lSLoop) -> {
      LoopedWalkProvider provider = (LoopedWalkProvider)entity;
      LoopedWalkModule module = provider.loopedWalkModule();
      lSLoop = BCMath.loop(lSLoop, 0.5F, 0.0F);
      if (BCMath.loop(module.prevLimbSwingLoop, 0.5F, 0.0F) > lSLoop) {
         provider.playLoopStepSound(entity.getOnPos(), blockstate);
      }

   };

   float hurtLoopLimbSwingFactor();

   LoopedWalkModule loopedWalkModule();

   default boolean operateWalkModule() {
      return this.loopedWalkModule() != null;
   }

   float getLoopWalkMultiplier();

   default LoopStepSoundPlayer lSSPlayer() {
      return LSP_DOUBLE;
   }

   void playLoopStepSound(BlockPos var1, BlockState var2);

   @FunctionalInterface
   public interface LoopStepSoundPlayer {
      void getLogic(LivingEntity var1, BlockState var2, float var3);
   }
}
