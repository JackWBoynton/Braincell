package bottomtextdanny.braincell.mod.entity.psyche.actions;

import bottomtextdanny.braincell.base.vector.DistanceCalc3;
import bottomtextdanny.braincell.mod.entity.psyche.Action;
import bottomtextdanny.braincell.mod.entity.psyche.pos_finder.MobPosProcessor;
import bottomtextdanny.braincell.mod.entity.psyche.targeting.*;
import bottomtextdanny.braincell.mod.entity.psyche.targeting.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.DoubleSupplier;
import java.util.function.ToDoubleFunction;

public class MoveReactionToEntityAction<T extends PathfinderMob, U extends Entity> extends Action<T> {
    public static int DEFAULT_PATH_REFRESH_RATE = 4;
    private final MobPosProcessor<U> posFinder;
    private final MobMatchPredicate<U> mobFinder;
    private ToDoubleFunction<U> moveSpeedByTarget;
    private SearchPredicate<U> searchPredicate;
    private BiConsumer<T, U> focusFoundCallout;
    private DoubleSupplier searchRange;
    @Nullable
    private U focus;
    @Nullable
    private Path goal;
    private int refreshRate;

    public MoveReactionToEntityAction(T mob, MobMatchPredicate<? super U> mobPredicate,
                                      MobPosProcessor<? super U> posFinder) {
        super(mob);
        this.refreshRate = DEFAULT_PATH_REFRESH_RATE;
        this.mobFinder = mobPredicate.cast();
        this.posFinder = posFinder.cast();
        this.moveSpeedByTarget = target -> 1.0;
        this.searchRange = () -> mob.getAttributeValue(Attributes.FOLLOW_RANGE);
        this.searchPredicate = SearchPredicates.nearestEntity().cast();
    }

    public MoveReactionToEntityAction<T, U> setRefreshRate(int refreshRate) {
        this.refreshRate = refreshRate;
        return this;
    }

    public MoveReactionToEntityAction<T, U> searchBy(SearchPredicate<? super U> predicate) {
        this.searchPredicate = (SearchPredicate<U>)predicate;
        return this;
    }

    public MoveReactionToEntityAction<T, U> speedByTarget(ToDoubleFunction<? super U> moveSpeedByTarget) {
        this.moveSpeedByTarget = (ToDoubleFunction<U>)moveSpeedByTarget;
        return this;
    }

    public MoveReactionToEntityAction<T, U> searchRange(DoubleSupplier searchRange) {
        this.searchRange = searchRange;
        return this;
    }

    public MoveReactionToEntityAction<T, U> onFocusFound(BiConsumer<? super T, ? super U> callout) {
        this.focusFoundCallout = (BiConsumer<T, U>) callout;
        return this;
    }

    public boolean canStart() {
        if (this.focus == null) {
            updateFocus();
        }

        U focus = this.focus;

        if (this.active() && !this.mob.isVehicle()
                && focus != null) {
            BlockPos goalPos = this.posFinder.compute(this.mob.blockPosition(), this.mob, UNSAFE_RANDOM, focus);

            if (goalPos == null) return false;

            this.goal = this.mob.getNavigation().createPath(goalPos, 0);

            return this.goal != null;
        } else {
            return false;
        }
    }

    protected void start() {
        this.mob.getNavigation().moveTo(this.goal, this.moveSpeedByTarget.applyAsDouble(this.focus));
    }

    protected void update() {
        if (this.ticksPassed % this.refreshRate == 0) {
            if (EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(this.focus)
                    && this.mob.getNavigation().isDone() && !this.goal.isDone()) {

                this.onPathUpdate();
            }
            else {
                this.goal = null;
                this.focus = null;
                this.mob.getNavigation().stop();
            }
        }
    }

    protected void onPathUpdate() {
        U entity = this.focus;
        this.mob.getNavigation().moveTo(this.goal, this.moveSpeedByTarget.applyAsDouble(entity));
    }

    public void onEnd() {
        super.onEnd();
        this.focus = null;
        this.goal = null;
        this.mob.getNavigation().stop();
    }

    public boolean shouldKeepGoing() {
        return this.active() && !this.mob.isVehicle() &&
                (this.goal != null && this.mob.getNavigation().isInProgress())
                && !this.mob.getNavigation().isStuck()
                && this.focus != null && this.focus.isAlive();
    }

    protected void updateFocus() {
        double range = this.searchRange.getAsDouble();
        U entity = focus;

        this.focus = this.searchPredicate.search(this.mob,
                (ServerLevel)this.mob.level,
                RangeTest.awayFrom(this.mob, range, DistanceCalc3.MANHATTAN),
                this.getTargetSearchArea(range), this.mobFinder.and(MobMatchPredicates.noCreativeOrSpectator()));

        if (focusFoundCallout != null && entity != focus) {
            focusFoundCallout.accept(mob, focus);
        }
    }

    public void inferFocus(U newFocus) {
        this.focus = newFocus;
    }

    protected Lazy<AABB> getTargetSearchArea(double p_26069_) {
        return Lazy.of(() -> {
            return this.mob.getBoundingBox().inflate(p_26069_, 4.0D, p_26069_);
        });
    }

    @Nullable
    public U getFocus() {
        return focus;
    }
}
