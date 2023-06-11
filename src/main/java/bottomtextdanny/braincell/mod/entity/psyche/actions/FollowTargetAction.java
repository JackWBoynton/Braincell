package bottomtextdanny.braincell.mod.entity.psyche.actions;

import bottomtextdanny.braincell.mod.entity.psyche.Action;
import bottomtextdanny.braincell.mod.world.helpers.ReachHelper;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import javax.annotation.Nullable;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.pathfinder.Path;

public class FollowTargetAction extends Action {
   public static int DEFAULT_PATH_REFRESH_RATE = 4;
   public static int INITIALIZE_REFRESH_RATE = 10;
   private final ToDoubleFunction moveSpeed;
   private int refreshRate;
   @Nullable
   private Predicate stopPredicate;
   private Path path;

   public FollowTargetAction(PathfinderMob mob, ToDoubleFunction moveSpeed) {
      super(mob);
      this.refreshRate = DEFAULT_PATH_REFRESH_RATE;
      this.moveSpeed = moveSpeed;
   }

   public FollowTargetAction setRefreshRate(int refreshRate) {
      this.refreshRate = refreshRate;
      return this;
   }

   public FollowTargetAction setStopPredicate(Predicate stopPredicate) {
      this.stopPredicate = stopPredicate;
      return this;
   }

   protected void start() {
      this.mob.getNavigation().moveTo(this.path, this.moveSpeed.applyAsDouble(this.mob.getTarget()));
      this.mob.setAggressive(true);
   }

   public boolean canStart() {
      LivingEntity livingEntity = this.mob.getTarget();
      if (this.active() && livingEntity != null && livingEntity.isAlive()) {
         this.path = this.mob.getNavigation().createPath(livingEntity, 0);
         return this.path != null;
      } else {
         return false;
      }
   }

   protected void update() {
      if (this.mob.getTarget() != null && this.mob.getTarget().isAlive() && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(this.mob.getTarget())) {
         this.mob.getLookControl().setLookAt(this.mob.getTarget(), 30.0F, 20.0F);
         if (this.ticksPassed % this.refreshRate == 0) {
            this.onPathUpdate();
         }

      } else {
         this.mob.getNavigation().stop();
      }
   }

   public void onPathUpdate() {
      LivingEntity livingentity = this.mob.getTarget();
      if ((this.stopPredicate == null || !this.stopPredicate.test(livingentity)) && !(ReachHelper.reachSqr(this.mob, this.mob.getTarget()) < 0.5F)) {
         this.mob.getNavigation().moveTo(livingentity, this.moveSpeed.applyAsDouble(livingentity));
      } else {
         this.mob.getNavigation().stop();
      }

   }

   public void onEnd() {
      super.onEnd();
      this.mob.getNavigation().stop();
   }

   public boolean shouldKeepGoing() {
      return this.active() && (this.mob.getNavigation().getPath() == null || !this.mob.getNavigation().isDone()) && this.mob.getTarget() != null && this.mob.getTarget().isAlive();
   }
}
