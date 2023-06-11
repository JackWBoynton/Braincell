package bottomtextdanny.braincell.mod.entity.modules.looped_walk;

import bottomtextdanny.braincell.BraincellHelper;
import bottomtextdanny.braincell.base.BCMath;
import bottomtextdanny.braincell.mod.entity.modules.EntityModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LoopedWalkModule extends EntityModule {
   public float limbSwingLoop;
   public float prevLimbSwingLoop;
   public float prevRenderLimbSwingAmount;
   public float renderLimbSwingAmount;

   public LoopedWalkModule(LivingEntity entity) {
      super(entity);
   }

   public void tick() {
      if (!((LivingEntity)this.entity).isInvisible()) {
         float limbSwingDistance = (float)BraincellHelper.RENDER_DIST2D_UTIL.start(((LivingEntity)this.entity).getX(), ((LivingEntity)this.entity).getZ()).get(((LivingEntity)this.entity).xo, ((LivingEntity)this.entity).zo);
         this.prevRenderLimbSwingAmount = this.renderLimbSwingAmount;
         this.renderLimbSwingAmount = (float)((double)this.renderLimbSwingAmount + (limbSwingDistance - this.renderLimbSwingAmount > 0.0F ? (double)(limbSwingDistance - this.renderLimbSwingAmount) / 1.5 : (double)((limbSwingDistance - this.renderLimbSwingAmount) / 4.0F)));
         this.prevLimbSwingLoop = this.limbSwingLoop;
         this.limbSwingLoop = BCMath.loop(this.limbSwingLoop, 1.0F, this.renderLimbSwingAmount * ((LoopedWalkProvider)this.provider).getLoopWalkMultiplier());
         BlockPos blockpos = ((LivingEntity)this.entity).getOnPos();
         BlockState blockstate = ((LivingEntity)this.entity).level.getBlockState(blockpos);
         ((LoopedWalkProvider)this.provider).lSSPlayer().getLogic((LivingEntity)this.entity, blockstate, this.limbSwingLoop);
      }

   }

}
