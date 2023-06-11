package bottomtextdanny.braincell.mod.entity.psyche.actions;

import bottomtextdanny.braincell.base.vector.DistanceCalc3;
import bottomtextdanny.braincell.mod.entity.psyche.Action;
import bottomtextdanny.braincell.mod.entity.psyche.pos_finder.MobPosProcessor;
import bottomtextdanny.braincell.mod.entity.psyche.targeting.MobMatchPredicate;
import bottomtextdanny.braincell.mod.entity.psyche.targeting.MobMatchPredicates;
import bottomtextdanny.braincell.mod.entity.psyche.targeting.RangeTest;
import bottomtextdanny.braincell.mod.entity.psyche.targeting.SearchPredicate;
import bottomtextdanny.braincell.mod.entity.psyche.targeting.SearchPredicates;
import java.util.function.BiConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.ToDoubleFunction;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraftforge.common.util.Lazy;

public class MoveReactionToEntityAction extends Action {
   public static int DEFAULT_PATH_REFRESH_RATE = 4;
   private final MobPosProcessor posFinder;
   private final MobMatchPredicate mobFinder;
   private ToDoubleFunction moveSpeedByTarget;
   private SearchPredicate searchPredicate;
   private BiConsumer focusFoundCallout;
   private DoubleSupplier searchRange;
   @Nullable
   private Entity focus;
   @Nullable
   private Path goal;
   private int refreshRate;

   public MoveReactionToEntityAction(PathfinderMob mob, MobMatchPredicate mobPredicate, MobPosProcessor posFinder) {
      super(mob);
      this.refreshRate = DEFAULT_PATH_REFRESH_RATE;
      this.mobFinder = mobPredicate.cast();
      this.posFinder = posFinder.cast();
      this.moveSpeedByTarget = (target) -> {
         return 1.0;
      };
      this.searchRange = () -> {
         return mob.getAttributeValue(Attributes.FOLLOW_RANGE);
      };
      this.searchPredicate = SearchPredicates.nearestEntity().cast();
   }

   public MoveReactionToEntityAction setRefreshRate(int refreshRate) {
      this.refreshRate = refreshRate;
      return this;
   }

   public MoveReactionToEntityAction searchBy(SearchPredicate predicate) {
      this.searchPredicate = predicate;
      return this;
   }

   public MoveReactionToEntityAction speedByTarget(ToDoubleFunction moveSpeedByTarget) {
      this.moveSpeedByTarget = moveSpeedByTarget;
      return this;
   }

   public MoveReactionToEntityAction searchRange(DoubleSupplier searchRange) {
      this.searchRange = searchRange;
      return this;
   }

   public MoveReactionToEntityAction onFocusFound(BiConsumer callout) {
      this.focusFoundCallout = callout;
      return this;
   }

   public boolean canStart() {
      if (this.focus == null) {
         this.updateFocus();
      }

      Entity focus = this.focus;
      if (this.active() && !this.mob.isVehicle() && focus != null) {
         BlockPos goalPos = this.posFinder.compute(this.mob.blockPosition(), this.mob, UNSAFE_RANDOM, focus);
         if (goalPos == null) {
            return false;
         } else {
            this.goal = this.mob.getNavigation().createPath(goalPos, 0);
            return this.goal != null;
         }
      } else {
         return false;
      }
   }

   protected void start() {
      this.mob.getNavigation().moveTo(this.goal, this.moveSpeedByTarget.applyAsDouble(this.focus));
   }

   protected void update() {
      if (this.ticksPassed % this.refreshRate == 0) {
         if (EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(this.focus) && this.mob.getNavigation().isDone() && !this.goal.isDone()) {
            this.onPathUpdate();
         } else {
            this.goal = null;
            this.focus = null;
            this.mob.getNavigation().stop();
         }
      }

   }

   protected void onPathUpdate() {
      Entity entity = this.focus;
      this.mob.getNavigation().moveTo(this.goal, this.moveSpeedByTarget.applyAsDouble(entity));
   }

   public void onEnd() {
      super.onEnd();
      this.focus = null;
      this.goal = null;
      this.mob.getNavigation().stop();
   }

   public boolean shouldKeepGoing() {
      return this.active() && !this.mob.isVehicle() && this.goal != null && this.mob.getNavigation().isInProgress() && !this.mob.getNavigation().isStuck() && this.focus != null && this.focus.isAlive();
   }

   protected void updateFocus() {
      double range = this.searchRange.getAsDouble();
      Entity entity = this.focus;
      this.focus = (Entity)this.searchPredicate.search(this.mob, (ServerLevel)this.mob.level, RangeTest.awayFrom(this.mob, range, DistanceCalc3.MANHATTAN), this.getTargetSearchArea(range), this.mobFinder.and(MobMatchPredicates.noCreativeOrSpectator()));
      if (this.focusFoundCallout != null && entity != this.focus) {
         this.focusFoundCallout.accept(this.mob, this.focus);
      }

   }

   public void inferFocus(Entity newFocus) {
      this.focus = newFocus;
   }

   protected Lazy getTargetSearchArea(double p_26069_) {
      return Lazy.of(() -> {
         return this.mob.getBoundingBox().inflate(p_26069_, 4.0, p_26069_);
      });
   }

   @Nullable
   public Entity getFocus() {
      return this.focus;
   }
}
