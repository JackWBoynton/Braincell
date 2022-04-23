package net.bottomtextdanny.braincell.mod.entity.psyche.actions.target;

import net.bottomtextdanny.braincell.base.scheduler.IntScheduler;
import net.bottomtextdanny.braincell.mod.entity.psyche.MarkedTimer;
import net.bottomtextdanny.braincell.mod.entity.psyche.actions.OccasionalThoughtAction;
import net.bottomtextdanny.braincell.mod._base.entity.psyche.input.ActionInputKey;
import net.bottomtextdanny.braincell.mod.entity.targeting.RangeTest;
import net.bottomtextdanny.braincell.mod.entity.targeting.SearchNearestPredicate;
import net.bottomtextdanny.braincell.mod.entity.targeting.TargetPredicate;
import net.bottomtextdanny.braincell.mod.entity.targeting.TargetPredicates;
import net.bottomtextdanny.braincell.base.vector.DistanceCalc;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;


public class LookForAttackTargetAction<E extends PathfinderMob> extends OccasionalThoughtAction<E> {
    public static final int DEFAULT_UPDATE_INTERVAL = 4;
    @Nullable
    protected TargetPredicate targetPredicate;
    protected SearchNearestPredicate searchPredicate;
    protected LivingEntity targetAsLocal;

    public LookForAttackTargetAction(E mob, IntScheduler updateInterval, TargetPredicate targeter, SearchNearestPredicate searcher) {
        super(mob, updateInterval);
        this.targetPredicate = targeter;
        this.searchPredicate = searcher;
    }

    protected Lazy<AABB> getTargetSearchArea(double p_26069_) {
        return Lazy.of(() -> this.mob.getBoundingBox().inflate(p_26069_, 4.0D, p_26069_));
    }

    protected double getFollowDistance() {
        return this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
    }

    @Override
    public void thoughtAction(int timeSinceBefore) {
        TargetPredicate sightPredicate = this.getPsyche().getInputs().getOfDefault(ActionInputKey.SEE_TARGET_PREDICATE);
        boolean oldTargetIsNull = this.targetAsLocal == null;
        if (this.getPsyche().getInputs().containsInput(ActionInputKey.MARKED_UNSEEN)) {
            MarkedTimer markedUnseen = this.getPsyche().getInputs().get(ActionInputKey.MARKED_UNSEEN).get();

            if (markedUnseen.testMark(this) || markedUnseen.testMark(null)) {
                if (this.targetAsLocal == null) markedUnseen.timer.end();
                else if (mob.getSensing().hasLineOfSight(this.targetAsLocal)) markedUnseen.timer.reset();
                else markedUnseen.timer.advance();
            }

            if (this.targetAsLocal != null) {
                boolean stillValid = this.targetPredicate.and(TargetPredicates.noCreativeOrSpectator()).test(this.mob, this.targetAsLocal);

                if (!markedUnseen.timer.hasEnded() && stillValid) {
                    markedUnseen.mark(this);
                } else {
                    //forgotten because unseen timer ended and target is not valid for instant targeting anymore.
                    if (markedUnseen.testMark(this)) markedUnseen.mark(null);
                    this.targetAsLocal = null;
                }
            } else {
                //looks for a new target.
                this.targetAsLocal = this.searchPredicate.search(this.mob, (ServerLevel) this.mob.level, RangeTest.awayFrom(this.mob, getFollowDistance(), DistanceCalc.MANHATTAN), getTargetSearchArea(getFollowDistance()), this.targetPredicate.and(sightPredicate).and(TargetPredicates.noCreativeOrSpectator()));

                if (this.targetAsLocal != null) {
                    //validation is already done in the searching.
                    markedUnseen.mark(this);

                    //stops current path the exact moment the mob discovers another target.
                    if (oldTargetIsNull) {
                        this.mob.getNavigation().stop();
                    }
                } else if (markedUnseen.testMark(this)) {
                    markedUnseen.mark(null);
                }
            }

            //tries to run custom call out if new target focus.
            if (markedUnseen.testMark(this) && oldTargetIsNull) {
                Runnable newTargetInput = getPsyche().getInputs().get(ActionInputKey.SET_TARGET_CALL);
                if (newTargetInput != null) {
                    newTargetInput.run();
                }
            }

            if (markedUnseen.testMark(this) || markedUnseen.testMark(null)) this.mob.setTarget(this.targetAsLocal);
        }

    }

    @Override
    public boolean cancelNext() {
        if (this.getPsyche().getInputs().containsInput(ActionInputKey.MARKED_UNSEEN)) {
            MarkedTimer markedUnseen = this.getPsyche().getInputs().get(ActionInputKey.MARKED_UNSEEN).get();
            return markedUnseen.testMark(this);
        }
        return false;
    }
}
