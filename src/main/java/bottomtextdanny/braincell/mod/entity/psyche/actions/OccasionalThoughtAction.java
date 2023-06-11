package bottomtextdanny.braincell.mod.entity.psyche.actions;

import bottomtextdanny.braincell.base.scheduler.IntScheduler;
import bottomtextdanny.braincell.mod.entity.psyche.Action;
import net.minecraft.world.entity.PathfinderMob;

public abstract class OccasionalThoughtAction extends Action {
   protected IntScheduler thoughtSchedule;
   private boolean updateTime;

   public OccasionalThoughtAction(PathfinderMob mob, IntScheduler timedSchedule) {
      super(mob);
      this.thoughtSchedule = timedSchedule;
   }

   public final void update() {
      this.updateTime = false;
      this.thoughtSchedule.incrementFreely(1);
      if (this.thoughtSchedule.hasEnded()) {
         this.thoughtAction(this.thoughtSchedule.current());
         this.thoughtSchedule.reset();
         this.updateTime = true;
      }

   }

   public abstract void thoughtAction(int var1);

   public boolean isUpdateTime() {
      return this.updateTime;
   }

   public boolean shouldKeepGoing() {
      return this.active();
   }
}
