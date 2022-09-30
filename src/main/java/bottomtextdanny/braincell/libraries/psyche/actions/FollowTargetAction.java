package bottomtextdanny.braincell.libraries.psyche.actions;

import bottomtextdanny.braincell.libraries.psyche.Action;
import bottomtextdanny.braincell.libraries.psyche.ReachHelper;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.pathfinder.Path;

import javax.annotation.Nullable;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

public class FollowTargetAction<E extends PathfinderMob> extends Action<E> {
    public static int DEFAULT_PATH_REFRESH_RATE = 4;
    public static int INITIALIZE_REFRESH_RATE = 10;
    private final ToDoubleFunction<LivingEntity> moveSpeed;
    private int refreshRate = DEFAULT_PATH_REFRESH_RATE;
    @Nullable
    private Predicate<LivingEntity> stopPredicate;
    private Path path;

    public FollowTargetAction(E mob, ToDoubleFunction<LivingEntity> moveSpeed) {
        super(mob);
        this.moveSpeed = moveSpeed;
    }

    public FollowTargetAction<E> setRefreshRate(int refreshRate) {
        this.refreshRate = refreshRate;
        return this;
    }

    public FollowTargetAction<E> setStopPredicate(Predicate<LivingEntity> stopPredicate) {
        this.stopPredicate = stopPredicate;
        return this;
    }

    @Override
    protected void start() {
        this.mob.getNavigation().moveTo(this.path, this.moveSpeed.applyAsDouble(this.mob.getTarget()));
        this.mob.setAggressive(true);
    }

    @Override
    public boolean canStart() {
        LivingEntity livingEntity = this.mob.getTarget();
        if (!active() || livingEntity == null || !livingEntity.isAlive()) {
            return false;
        } else {
            this.path = this.mob.getNavigation().createPath(livingEntity, 0);
            return this.path != null;
        }
    }

    @Override
    protected void update() {
        E mob = this.mob;
        LivingEntity target = mob.getTarget();

        if (target == null || !target.isAlive() || !EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
            mob.getNavigation().stop();
            return;
        }

        mob.getLookControl().setLookAt(target, 30.0F, 20.0F);

        if (this.ticksPassed % this.refreshRate == 0) {
            onPathUpdate();
        }
    }

    public void onPathUpdate() {
        LivingEntity livingentity = this.mob.getTarget();

        if (this.stopPredicate != null && this.stopPredicate.test(livingentity) || ReachHelper.reachSqr(this.mob, this.mob.getTarget()) < 0.5F) {
            this.mob.getNavigation().stop();
        } else {
            this.mob.getNavigation().moveTo(livingentity, this.moveSpeed.applyAsDouble(livingentity));
        }
    }

    @Override
    public void onEnd() {
        super.onEnd();
        this.mob.getNavigation().stop();
    }

    @Override
    public boolean shouldKeepGoing() {
        E mob = this.mob;
        return active() && !(mob.getNavigation().getPath() != null && mob.getNavigation().isDone()) && mob.getTarget() != null && mob.getTarget().isAlive();
    }
}
