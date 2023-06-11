package bottomtextdanny.braincell.mod.entity.psyche.actions;

import bottomtextdanny.braincell.mod.entity.psyche.Action;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class AlfWanderAirAction extends Action {
   private final float moveSpeed;

   public AlfWanderAirAction(PathfinderMob mob, float moveSpeed) {
      super(mob);
      this.moveSpeed = moveSpeed;
   }

   public boolean canStart() {
      return this.active() && this.mob.getNavigation().isDone();
   }

   protected void start() {
      Vec3 vec3 = this.findPos();
      if (vec3 != null) {
         this.mob.getNavigation().moveTo(this.mob.getNavigation().createPath(new BlockPos(vec3), 1), (double)this.moveSpeed);
      }

   }

   protected void update() {
      Path path = this.mob.getNavigation().getPath();
      if (path != null && path.getNextNodeIndex() < path.getNodeCount()) {
         Vec3 vec = Vec3.atCenterOf(path.getNextNodePos());
         this.mob.getLookControl().setLookAt(vec.x, vec.y, vec.z, 180.0F, 10.0F);
      }

   }

   public boolean shouldKeepGoing() {
      return this.active() && this.mob.getNavigation().isInProgress();
   }

   public void onEnd() {
      super.onEnd();
      this.mob.getNavigation().stop();
   }

   @Nullable
   private Vec3 findPos() {
      Vec3 vec3 = this.mob.getViewVector(14.0F).multiply(1.0, 0.2, 1.0);
      Vec3 vec31 = HoverRandomPos.getPos(this.mob, 8, 16, vec3.x, vec3.z, 6.2831855F, 5, 4);
      return vec31 != null ? vec31 : AirRandomPos.getPosTowards(this.mob, 8, 2, -4, vec3, 6.283185307179586);
   }
}
