package bottomtextdanny.braincell.libraries.psyche.actions.target;

import bottomtextdanny.braincell.base.scheduler.IntScheduler;
import bottomtextdanny.braincell.libraries.chart.help.Distance3s;
import bottomtextdanny.braincell.libraries.psyche.targeting.MobMatchPredicate;
import bottomtextdanny.braincell.libraries.psyche.targeting.RangeTest;
import bottomtextdanny.braincell.libraries.psyche.targeting.SearchPredicate;
import bottomtextdanny.braincell.libraries.psyche.targeting.TargetPredicates;
import bottomtextdanny.braincell.libraries.psyche.input.ActionInputKey;
import bottomtextdanny.braincell.libraries.psyche.input.ActionInputs;
import bottomtextdanny.braincell.libraries.psyche.actions.OccasionalThoughtAction;
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
    protected MobMatchPredicate<LivingEntity> targetPredicate;
    protected SearchPredicate<LivingEntity> searchPredicate;
    //protected LivingEntity targetAsLocal;

    public LookForAttackTargetAction(E mob, IntScheduler updateInterval,
                                     MobMatchPredicate<? super LivingEntity> targeter,
                                     SearchPredicate<? extends LivingEntity> searcher) {
        super(mob, updateInterval);
        this.targetPredicate = targeter.cast();
        this.searchPredicate = searcher.hackyCast();
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
//        ActionInputs inputs = getPsyche().getInputs();
//
//        if (inputs.containsInput(ActionInputKey.BLINDTRACKING_TIMER)) {
//            MobMatchPredicate<LivingEntity> sightPredicate = inputs.getOfDefault(ActionInputKey.SEE_TARGET_PREDICATE);
//            boolean previousLocalTargetIsNull = this.targetAsLocal == null;
//            MarkedTimer markedUnseen = inputs.get(ActionInputKey.BLINDTRACKING_TIMER).get();
//
//            processTarget(sightPredicate, markedUnseen, previousLocalTargetIsNull);
//        }

        if (mob.getTarget() == null) {
            LivingEntity target = getTarget();
            if (target != null) {
                mob.setTarget(target);
            }
        }
    }

    @Nullable
    protected LivingEntity getTarget() {
        MobMatchPredicate<LivingEntity> sightPredicate = getPsyche().getInputs().getOfDefault(ActionInputKey.SEE_TARGET_PREDICATE);
        return this.searchPredicate.search(this.mob, (ServerLevel) this.mob.level, RangeTest.awayFrom(this.mob, getFollowDistance(), Distance3s.d3Manhat()), getTargetSearchArea(getFollowDistance()), this.targetPredicate.and(sightPredicate).and(TargetPredicates.noCreativeOrSpectator()));
    }

//    protected void processTarget(MobMatchPredicate<LivingEntity> sightPredicate,
//                                 MarkedTimer markedUnseen,
//                                 boolean previousLocalTargetIsNull) {
//        LivingEntity prevLocalTarget = targetAsLocal;
//        boolean prevLocalTargetIsValid = getPsyche().getInputs().getOfDefault(ActionInputKey.TARGET_VALIDATOR).test(this.mob, this.mob.getTarget());
//        if (!(markedUnseen.isMarkedBy(this))
//                && prevLocalTargetIsValid) {
//            return;
//        }
//
//        //if local target is not chosen then unseen timer is forced to end
//        //or else if there is a local target chosen and the mob can see it, unseen timer resets
//        //or if there is a chosen target, and it can not be seen by the mob, the unseen timer advances.
//        if (markedUnseen.isUnmarkedOrMarkedBy(this)) {
//            if (this.targetAsLocal == null) markedUnseen.timer.end();
//            else if (mob.getSensing().hasLineOfSight(this.targetAsLocal)) markedUnseen.timer.reset();
//            else markedUnseen.timer.advance();
//        }
//
//        if (this.targetAsLocal != null) {
//            boolean previousTargetIsStillValid = this.targetPredicate.and(TargetPredicates.noCreativeOrSpectator()).test(this.mob, this.targetAsLocal);
//
//            //if target is still valid, and it is not unseen yet, we assert that this targeting action is our mark.
//            if (!markedUnseen.timer.hasEnded() && previousTargetIsStillValid) {
//                markedUnseen.setMarkedBy(this);
//            }
//            //or else we just free the mark (if it was marked by this targeting action).
//            else {
//                if (markedUnseen.isMarkedBy(this)) markedUnseen.unmark();
//                this.targetAsLocal = null;
//            }
//        }
//
//        //if previous target is null, or it was forgotten just now, we look for another.
//        if (this.targetAsLocal == null) {
//            //fetches raw new target, can return null.
//            //validation of this new target should be already handled by this action's SearchNearestPredicate.
//            this.targetAsLocal = this.searchPredicate.search(this.mob, (ServerLevel) this.mob.level, RangeTest.awayFrom(this.mob, getFollowDistance(), Distance3s.d3Manhat()), getTargetSearchArea(getFollowDistance()), this.targetPredicate.and(sightPredicate).and(TargetPredicates.noCreativeOrSpectator()));
//
//
//            //if new target is not null then this new target is our new local target.
//            if (this.targetAsLocal != null) {
//                //we assert that the mark belongs to here then.
//                markedUnseen.setMarkedBy(this);
//
//                //if we did not have a target before, we stop entity this current path, so activities like chasing are instant.
//                if (previousLocalTargetIsNull) {
//                    this.mob.getNavigation().stop();
//                }
//            }
//
//            //if previous local target was null or discarded, and a new target could not be found, we just remove our mark (if it is ours).
//            else if (markedUnseen.isMarkedBy(this)) {
//                markedUnseen.unmark();
//            }
//        }
//
//        //tries to run custom call out if new target.
//        if (markedUnseen.isMarkedBy(this) && previousLocalTargetIsNull) {
//            Runnable newTargetInput = getPsyche().getInputs().get(ActionInputKey.SET_TARGET_CALL);
//            if (newTargetInput != null) newTargetInput.run();
//            if (this.findTargetCallOut != null)
//                this.findTargetCallOut.accept(this.mob, this.targetAsLocal);
//        }
//
//        //if (this.targetAsLocal == null && !previousLocalTargetIsNull) this.mob.setTarget(null);
//
//        //if local target is valid and the mark is ours, we finally change the psyche's target.
//        if (markedUnseen.isMarkedBy(this)) {
//            if (targetAsLocal != null) {
//                this.mob.setTarget(this.targetAsLocal);
//            } else if (prevLocalTarget == this.mob.getTarget() && !prevLocalTargetIsValid) {
//                this.mob.setTarget(null);
//            }
//        }
//    }

    @Override
    public boolean cancelNext() {
        return mob.getTarget() != null;
    }
}
