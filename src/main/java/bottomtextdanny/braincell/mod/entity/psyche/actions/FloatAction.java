package bottomtextdanny.braincell.mod.entity.psyche.actions;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.Vec3;

public class FloatAction extends ConstantThoughtAction {
   public static final float DEFAULT_FLOAT_HEIGHT = 0.5F;
   public static final int DETECT_GROUND_REFRESH_RATE = 14;
   private final float floatHeight;
   private boolean cachedDetectedGroundState;

   public FloatAction(PathfinderMob mob, float floatHeight) {
      super(mob, (Consumer)null);
      this.floatHeight = floatHeight;
      mob.getNavigation().setCanFloat(true);
      this.cachedDetectedGroundState = true;
   }

   public FloatAction(PathfinderMob mob) {
      this(mob, 0.5F);
   }

   protected void update() {
      double fluidHeight = this.mob.getFluidHeight(FluidTags.WATER);
      if (fluidHeight > 0.15 && this.ticksPassed % 14 == 0) {
         float reducedHeight = this.mob.getEyeHeight() - 0.15F;
         this.cachedDetectedGroundState = this.detectGround(reducedHeight);
      }

      if ((this.mob.isInWater() || this.mob.isInLava()) && (this.mob.horizontalCollision || !this.cachedDetectedGroundState && fluidHeight > (double)this.floatHeight) && this.mob.getRandom().nextFloat() < 0.8F) {
         this.mob.getJumpControl().jump();
      }

   }

   public boolean detectGround(float height) {
      if (!this.mob.isInLava() && this.mob.getFluidHeight(FluidTags.WATER) < (double)height) {
         float gap = (float)((double)height - this.mob.getFluidHeight(FluidTags.WATER));
         Vec3 gapVec = this.mob.position().subtract(0.0, (double)gap, 0.0);
         BlockPos gapPos = new BlockPos(gapVec);
         double delta = this.mob.level.getBlockState(gapPos).getCollisionShape(this.mob.level, gapPos).max(Axis.Y);
         if (delta <= 0.0) {
            BlockPos gapPosBelow = gapPos.below();
            double lowerDelta = this.mob.level.getBlockState(gapPosBelow).getCollisionShape(this.mob.level, gapPosBelow).max(Axis.Y);
            delta = lowerDelta - 1.0;
         }

         return delta > 0.0 && delta + (double)gapPos.getY() - 1.0 < gapVec.y;
      } else {
         return false;
      }
   }
}
