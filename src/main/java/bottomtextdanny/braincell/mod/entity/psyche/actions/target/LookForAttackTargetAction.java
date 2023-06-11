package bottomtextdanny.braincell.mod.entity.psyche.actions.target;

import bottomtextdanny.braincell.base.scheduler.IntScheduler;
import bottomtextdanny.braincell.base.vector.DistanceCalc3;
import bottomtextdanny.braincell.mod.entity.psyche.MarkedTimer;
import bottomtextdanny.braincell.mod.entity.psyche.actions.OccasionalThoughtAction;
import bottomtextdanny.braincell.mod.entity.psyche.input.ActionInputKey;
import bottomtextdanny.braincell.mod.entity.psyche.input.ActionInputs;
import bottomtextdanny.braincell.mod.entity.psyche.targeting.MobMatchPredicate;
import bottomtextdanny.braincell.mod.entity.psyche.targeting.RangeTest;
import bottomtextdanny.braincell.mod.entity.psyche.targeting.SearchPredicate;
import bottomtextdanny.braincell.mod.entity.psyche.targeting.TargetPredicates;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.util.Lazy;

public class LookForAttackTargetAction extends OccasionalThoughtAction {
   public static final int DEFAULT_UPDATE_INTERVAL = 4;
   @Nullable
   private BiConsumer findTargetCallOut;
   @Nullable
   protected MobMatchPredicate targetPredicate;
   protected SearchPredicate searchPredicate;
   protected LivingEntity targetAsLocal;

   public LookForAttackTargetAction(PathfinderMob mob, IntScheduler updateInterval, MobMatchPredicate targeter, SearchPredicate searcher) {
      super(mob, updateInterval);
      this.targetPredicate = targeter.cast();
      this.searchPredicate = searcher.hackyCast();
   }

   public LookForAttackTargetAction findTargetCallOut(BiConsumer callOut) {
      this.findTargetCallOut = callOut;
      return this;
   }

   protected Lazy getTargetSearchArea(double p_26069_) {
      return Lazy.of(() -> {
         return this.mob.getBoundingBox().inflate(p_26069_, 4.0, p_26069_);
      });
   }

   protected double getFollowDistance() {
      return this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
   }

   public void thoughtAction(int timeSinceBefore) {
      ActionInputs inputs = this.getPsyche().getInputs();
      if (inputs.containsInput(ActionInputKey.MARKED_UNSEEN)) {
         MobMatchPredicate sightPredicate = (MobMatchPredicate)inputs.getOfDefault(ActionInputKey.SEE_TARGET_PREDICATE);
         boolean previousLocalTargetIsNull = this.targetAsLocal == null;
         MarkedTimer markedUnseen = (MarkedTimer)((Supplier)inputs.get(ActionInputKey.MARKED_UNSEEN)).get();
         this.processTarget(sightPredicate, markedUnseen, previousLocalTargetIsNull);
      }

   }

   protected void processTarget(MobMatchPredicate sightPredicate, MarkedTimer markedUnseen, boolean previousLocalTargetIsNull) {
      if (markedUnseen.isMarkedBy(this) || !((MobMatchPredicate)this.getPsyche().getInputs().getOfDefault(ActionInputKey.TARGET_VALIDATOR)).test(this.mob, this.mob.getTarget())) {
         if (markedUnseen.isUnmarkedOrMarkedBy(this)) {
            if (this.targetAsLocal == null) {
               markedUnseen.timer.end();
            } else if (this.mob.getSensing().hasLineOfSight(this.targetAsLocal)) {
               markedUnseen.timer.reset();
            } else {
               markedUnseen.timer.advance();
            }
         }

         if (this.targetAsLocal != null) {
            boolean previousTargetIsStillValid = this.targetPredicate.and(TargetPredicates.noCreativeOrSpectator()).test(this.mob, this.targetAsLocal);
            if (!markedUnseen.timer.hasEnded() && previousTargetIsStillValid) {
               markedUnseen.setMarkedBy(this);
            } else {
               if (markedUnseen.isMarkedBy(this)) {
                  markedUnseen.unmark();
               }

               this.targetAsLocal = null;
            }
         }

         if (this.targetAsLocal == null) {
            this.targetAsLocal = (LivingEntity)this.searchPredicate.search(this.mob, (ServerLevel)this.mob.level, RangeTest.awayFrom(this.mob, this.getFollowDistance(), DistanceCalc3.MANHATTAN), this.getTargetSearchArea(this.getFollowDistance()), this.targetPredicate.and(sightPredicate).and(TargetPredicates.noCreativeOrSpectator()));
            if (this.targetAsLocal != null) {
               markedUnseen.setMarkedBy(this);
               if (previousLocalTargetIsNull) {
                  this.mob.getNavigation().stop();
               }
            } else if (markedUnseen.isMarkedBy(this)) {
               markedUnseen.unmark();
            }
         }

         if (markedUnseen.isMarkedBy(this) && previousLocalTargetIsNull) {
            Runnable newTargetInput = (Runnable)this.getPsyche().getInputs().get(ActionInputKey.SET_TARGET_CALL);
            if (newTargetInput != null) {
               newTargetInput.run();
            }

            if (this.findTargetCallOut != null) {
               this.findTargetCallOut.accept(this.mob, this.targetAsLocal);
            }
         }

         if (this.targetAsLocal != null && markedUnseen.isMarkedBy(this)) {
            this.mob.setTarget(this.targetAsLocal);
         }

      }
   }

   public boolean cancelNext() {
      if (this.getPsyche().getInputs().containsInput(ActionInputKey.MARKED_UNSEEN)) {
         MarkedTimer markedUnseen = (MarkedTimer)((Supplier)this.getPsyche().getInputs().get(ActionInputKey.MARKED_UNSEEN)).get();
         return markedUnseen.isMarkedBy(this);
      } else {
         return false;
      }
   }
}
