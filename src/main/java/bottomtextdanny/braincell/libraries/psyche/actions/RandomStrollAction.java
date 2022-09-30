package bottomtextdanny.braincell.libraries.psyche.actions;

import bottomtextdanny.braincell.base.value_mapper.RandomIntegerMapper;
import bottomtextdanny.braincell.libraries.psyche.Action;
import bottomtextdanny.braincell.libraries.psyche.pos_finder.MobPosProcessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class RandomStrollAction extends Action<PathfinderMob> {
    public static final RandomIntegerMapper DEFAULT_STROLL_TIME = RandomIntegerMapper.of(120, 150);
    protected RandomIntegerMapper strollTime;
    private final MobPosProcessor<?> posProcessor;
    protected int timeTillStroll;
    protected float speedModifier = 1.0F;
    protected Path path;

    public RandomStrollAction(PathfinderMob mob, MobPosProcessor<?> posProcessor, RandomIntegerMapper strollTime) {
        super(mob);
        this.strollTime = strollTime;
        this.posProcessor = posProcessor;
        this.timeTillStroll = strollTime.map(UNSAFE_RANDOM);
    }

    public void setSpeedModifier(float newSpeed) {
        this.speedModifier = newSpeed;
    }

    @Override
    public boolean canStart() {
        if (!active()) return false;

        if (this.mob.isVehicle()) {
            return false;
        } else {
            if (this.timeTillStroll > 0) {
                this.timeTillStroll--;
                return false;
            }

            this.timeTillStroll = this.strollTime.map(UNSAFE_RANDOM);

            Vec3 randomPos = this.getRandomPosition();

            if (randomPos == null) {
                return false;
            } else {
                path = mob.getNavigation().createPath(randomPos.x, randomPos.y, randomPos.z, 0);
                return path != null;
            }
        }

    }

    public void start() {
        this.mob.getNavigation().moveTo(path, this.speedModifier);
    }

    @Override
    public boolean shouldKeepGoing() {
        return !this.mob.getNavigation().isDone() && !mob.getNavigation().isStuck()
                && !this.mob.isVehicle() && active();
    }

    @Override
    public void onEnd() {
        super.onEnd();
        this.mob.getNavigation().stop();
    }

    @Nullable
    protected Vec3 getRandomPosition() {
        BlockPos pos = this.posProcessor.compute(this.mob.blockPosition(), this.mob, UNSAFE_RANDOM, null);
        if (pos != null) {
            return Vec3.atCenterOf(pos);
        }
        return null;
    }
}
