package bottomtextdanny.braincell.mod.entity.psyche.actions;

import bottomtextdanny.braincell.mod.entity.psyche.Action;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class AlfFlyFromLookAction extends Action {
   private final float deltaSearchPoint;
   private final float moveSpeed;

   public AlfFlyFromLookAction(PathfinderMob mob, float delta, float moveSpeed) {
      super(mob);
      this.moveSpeed = moveSpeed;
      this.deltaSearchPoint = delta;
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
         Vec3 var2 = Vec3.atCenterOf(path.getNextNodePos());
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
      Vec3 vec3 = this.mob.getViewVector(1.0F).scale((double)this.deltaSearchPoint).scale(-1.0);
      Vec3 vec31 = HoverRandomPos.getPos(this.mob, 7, 10, vec3.x, vec3.z, 1.5707964F, 5, 4);
      return vec31 != null ? vec31 : AirAndWaterRandomPos.getPos(this.mob, 8, 0, -4, vec3.x, vec3.z, 1.5707963705062866);
   }
}
