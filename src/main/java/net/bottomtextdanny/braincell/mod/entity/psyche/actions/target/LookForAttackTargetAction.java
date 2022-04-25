package net.bottomtextdanny.braincell.mod.entity.psyche.actions.target;

import net.bottomtextdanny.braincell.base.scheduler.IntScheduler;
import net.bottomtextdanny.braincell.base.vector.DistanceCalc;
import net.bottomtextdanny.braincell.mod.entity.psyche.input.ActionInputKey;
import net.bottomtextdanny.braincell.mod.entity.psyche.input.ActionInputs;
import net.bottomtextdanny.braincell.mod.entity.psyche.MarkedTimer;
import net.bottomtextdanny.braincell.mod.entity.psyche.actions.OccasionalThoughtAction;
import net.bottomtextdanny.braincell.mod.entity.psyche.targeting.RangeTest;
import net.bottomtextdanny.braincell.mod.entity.psyche.targeting.SearchNearestPredicate;
import net.bottomtextdanny.braincell.mod.entity.psyche.targeting.TargetPredicate;
import net.bottomtextdanny.braincell.mod.entity.psyche.targeting.TargetPredicates;
import net.bottomtextdanny.braincell.mod.world.helpers.CombatHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;


public class LookForAttackTargetAction<E extends PathfinderMob> extends OccasionalThoughtAction<E> {
    public static final int DEFAULT_UPDATE_INTERVAL = 4;
    @Nullable
    private BiConsumer<Mob, LivingEntity> findTargetCallOut;
    @Nullable
    protected TargetPredicate targetPredicate;
    protected SearchNearestPredicate searchPredicate;
    protected LivingEntity targetAsLocal;

    public LookForAttackTargetAction(E mob, IntScheduler updateInterval, TargetPredicate targeter, SearchNearestPredicate searcher) {
        super(mob, updateInterval);
        this.targetPredicate = targeter;
        this.searchPredicate = searcher;
    }

    public LookForAttackTargetAction<E> findTargetCallOut(BiConsumer<Mob, LivingEntity> callOut) {
        this.findTargetCallOut = callOut;
        return this;
    }

    protected Lazy<AABB> getTargetSearchArea(double p_26069_) {
        return Lazy.of(() -> this.mob.getBoundingBox().inflate(p_26069_, 4.0D, p_26069_));
    }

    protected double getFollowDistance() {
        return this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
    }

    @Override
    public void thoughtAction(int timeSinceBefore) {
        ActionInputs inputs = getPsyche().getInputs();

        if (inputs.containsInput(ActionInputKey.MARKED_UNSEEN)) {
            TargetPredicate sightPredicate = inputs.getOfDefault(ActionInputKey.SEE_TARGET_PREDICATE);
            boolean previousLocalTargetIsNull = this.targetAsLocal == null;
            MarkedTimer markedUnseen = inputs.get(ActionInputKey.MARKED_UNSEEN).get();

            processTarget(sightPredicate, markedUnseen, previousLocalTargetIsNull);
        }
    }

    protected void processTarget(TargetPredicate sightPredicate,
                                 MarkedTimer markedUnseen,
                                 boolean previousLocalTargetIsNull) {

        if (!(markedUnseen.isMarkedBy(this))
                && getPsyche().getInputs().getOfDefault(ActionInputKey.TARGET_VALIDATOR).test(this.mob, this.mob.getTarget())) {
            return;
        }

        //if local target is not chosen then unseen timer is forced to end
        //or else if there is a local target chosen and the mob can see it, unseen timer resets
        //or if there is a chosen target, and it can not be seen by the mob, the unseen timer advances.
        if (markedUnseen.isUnmarkedOrMarkedBy(this)) {
            if (this.targetAsLocal == null) markedUnseen.timer.end();
            else if (mob.getSensing().hasLineOfSight(this.targetAsLocal)) markedUnseen.timer.reset();
            else markedUnseen.timer.advance();
        }

        if (this.targetAsLocal != null) {
            boolean previousTargetIsStillValid = this.targetPredicate.and(TargetPredicates.noCreativeOrSpectator()).test(this.mob, this.targetAsLocal);

            //if target is still valid, and it is not unseen yet, we assert that this targeting action is our mark.
            if (!markedUnseen.timer.hasEnded() && previousTargetIsStillValid) {
                markedUnseen.setMarkedBy(this);
            }
            //or else we just free the mark (if it was marked by this targeting action).
            else {
                if (markedUnseen.isMarkedBy(this)) markedUnseen.unmark();
                this.targetAsLocal = null;
            }
        }

        //if previous target is null, or it was forgotten just now, we look for another.
        if (this.targetAsLocal == null) {
            //fetches raw new target, can return null.
            //validation of this new target should be already handled by this action's SearchNearestPredicate.
            this.targetAsLocal = this.searchPredicate.search(this.mob, (ServerLevel) this.mob.level, RangeTest.awayFrom(this.mob, getFollowDistance(), DistanceCalc.MANHATTAN), getTargetSearchArea(getFollowDistance()), this.targetPredicate.and(sightPredicate).and(TargetPredicates.noCreativeOrSpectator()));


            //if new target is not null then this new target is our new local target.
            if (this.targetAsLocal != null) {
                //we assert that the mark belongs to here then.
                markedUnseen.setMarkedBy(this);

                //if we did not have a target before, we stop entity this current path, so activities like chasing are instant.
                if (previousLocalTargetIsNull) {
                    this.mob.getNavigation().stop();
                }
            }

            //if previous local target was null or discarded, and a new target could not be found, we just remove our mark (if it is ours).
            else if (markedUnseen.isMarkedBy(this)) {
                markedUnseen.unmark();
            }
        }

        //tries to run custom call out if new target.
        if (markedUnseen.isMarkedBy(this) && previousLocalTargetIsNull) {
            Runnable newTargetInput = getPsyche().getInputs().get(ActionInputKey.SET_TARGET_CALL);
            if (newTargetInput != null) newTargetInput.run();
            if (this.findTargetCallOut != null)
                this.findTargetCallOut.accept(this.mob, this.targetAsLocal);
        }

        if (this.targetAsLocal == null && !previousLocalTargetIsNull) this.mob.setTarget(null);

        //if local target is valid and the mark is ours, we finally change the real mob's target.
        if (this.targetAsLocal != null && markedUnseen.isMarkedBy(this))
            this.mob.setTarget(this.targetAsLocal);
    }

    @Override
    public boolean cancelNext() {
        if (this.getPsyche().getInputs().containsInput(ActionInputKey.MARKED_UNSEEN)) {
            MarkedTimer markedUnseen = this.getPsyche().getInputs().get(ActionInputKey.MARKED_UNSEEN).get();
            return markedUnseen.isMarkedBy(this);
        }
        return false;
    }
}
