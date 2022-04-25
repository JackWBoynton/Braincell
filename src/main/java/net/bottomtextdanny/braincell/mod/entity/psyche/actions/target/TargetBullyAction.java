package net.bottomtextdanny.braincell.mod.entity.psyche.actions.target;

import net.bottomtextdanny.braincell.mod._base.entity.psyche.input.ActionInputKey;
import net.bottomtextdanny.braincell.mod.entity.psyche.actions.ConstantThoughtAction;
import net.bottomtextdanny.braincell.mod.entity.targeting.TargetPredicate;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;

import javax.annotation.Nullable;

public class TargetBullyAction extends ConstantThoughtAction<PathfinderMob> {
    private final TargetPredicate targetParameters;
    @Nullable
    private LivingEntity localTarget;
    private int timestamp;

    public TargetBullyAction(PathfinderMob mob, TargetPredicate targetParameters) {
        super(mob, null);
        this.targetParameters = targetParameters;
    }

    @Override
    protected void update() {
        if (!active()) return;
        if (!this.mob.isWithinRestriction()) return;

        LivingEntity previousState = this.localTarget;

        if (this.mob.getLastHurtByMob() == null || !this.mob.getLastHurtByMob().isAlive()) {
            this.localTarget = null;
        } else if (this.timestamp != this.mob.getLastHurtByMobTimestamp()) {
            if (this.targetParameters.test(this.mob, this.mob.getLastHurtByMob()) && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(this.mob)) {

                this.mob.setTarget(this.mob.getLastHurtByMob());
                this.localTarget = this.mob.getTarget();
            }
            this.timestamp = this.mob.getLastHurtByMobTimestamp();
        }

        if (this.localTarget != null && this.localTarget.isAlive() && previousState == null) {
            Runnable newTargetInput = getPsyche().getInputs().get(ActionInputKey.SET_TARGET_CALL);
            if (newTargetInput != null) {
                newTargetInput.run();
            }
        }
    }

    @Override
    public boolean cancelNext() {
        return this.localTarget != null && this.mob.getTarget() == this.localTarget;
    }
}
