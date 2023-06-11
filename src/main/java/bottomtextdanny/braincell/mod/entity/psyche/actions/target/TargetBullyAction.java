package bottomtextdanny.braincell.mod.entity.psyche.actions.target;

import bottomtextdanny.braincell.mod.entity.psyche.actions.ConstantThoughtAction;
import bottomtextdanny.braincell.mod.entity.psyche.input.ActionInputKey;
import bottomtextdanny.braincell.mod.entity.psyche.targeting.MobMatchPredicate;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;

public class TargetBullyAction extends ConstantThoughtAction {
   private final MobMatchPredicate targetParameters;
   @Nullable
   private BiConsumer findTargetCallOut;
   @Nullable
   private LivingEntity localTarget;
   private int timestamp;

   public TargetBullyAction(PathfinderMob mob, MobMatchPredicate targetParameters) {
      super(mob, (Consumer)null);
      this.targetParameters = targetParameters.cast();
   }

   public TargetBullyAction findTargetCallOut(BiConsumer callOut) {
      this.findTargetCallOut = callOut;
      return this;
   }

   protected void update() {
      if (this.active()) {
         if (this.mob.isWithinRestriction()) {
            LivingEntity previousState = this.localTarget;
            if (this.mob.getLastHurtByMob() != null && this.mob.getLastHurtByMob().isAlive()) {
               if (this.timestamp != this.mob.getLastHurtByMobTimestamp()) {
                  if (this.targetParameters.test(this.mob, this.mob.getLastHurtByMob()) && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(this.mob)) {
                     this.mob.setTarget(this.mob.getLastHurtByMob());
                     this.localTarget = this.mob.getTarget();
                  }

                  this.timestamp = this.mob.getLastHurtByMobTimestamp();
               }
            } else {
               this.localTarget = null;
            }

            if (this.localTarget != null && this.localTarget.isAlive() && previousState == null) {
               Runnable newTargetInput = (Runnable)this.getPsyche().getInputs().get(ActionInputKey.SET_TARGET_CALL);
               if (newTargetInput != null) {
                  newTargetInput.run();
               }

               if (this.findTargetCallOut != null) {
                  this.findTargetCallOut.accept(this.mob, this.localTarget);
               }
            }

         }
      }
   }

   public boolean cancelNext() {
      return this.localTarget != null && this.mob.getTarget() == this.localTarget;
   }
}
