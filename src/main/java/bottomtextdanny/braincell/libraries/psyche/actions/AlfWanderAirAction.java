package bottomtextdanny.braincell.libraries.psyche.actions;

import bottomtextdanny.braincell.base.BCMath;
import bottomtextdanny.braincell.libraries.psyche.Action;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class AlfWanderAirAction<E extends PathfinderMob> extends Action<E> {
    private final float moveSpeed;

    public AlfWanderAirAction(E mob, float moveSpeed) {
        super(mob);
        this.moveSpeed = moveSpeed;
    }

    @Override
    public boolean canStart() {
        return active() && this.mob.getNavigation().isDone();
    }

    @Override
    protected void start() {
        Vec3 vec3 = this.findPos();
        if (vec3 != null) {
            this.mob.getNavigation().moveTo(this.mob.getNavigation().createPath(new BlockPos(vec3), 1), this.moveSpeed);
        }
    }


    @Override
    protected void update() {
        Path path = this.mob.getNavigation().getPath();
        if (path != null) {
            if (path.getNextNodeIndex() < path.getNodeCount()) {
                Vec3 vec = Vec3.atCenterOf(path.getNextNodePos());
                this.mob.getLookControl().setLookAt(vec.x, vec.y, vec.z, 180.0F, 10.0F);
            }
        }
    }

    @Override
    public boolean shouldKeepGoing() {
        return active() && this.mob.getNavigation().isInProgress();
    }

    @Override
    public void onEnd() {
        super.onEnd();
        this.mob.getNavigation().stop();

    }

    @Nullable
    private Vec3 findPos() {
        Vec3 vec3 = this.mob.getViewVector(14.0F).multiply(1.0, 0.2, 1.0);
        Vec3 vec31 = HoverRandomPos.getPos(this.mob, 8, 16, vec3.x, vec3.z, BCMath.FPI_BY_TWO, 5, 4);
        return vec31 != null ? vec31 : AirRandomPos.getPosTowards(this.mob, 8, 2, -4, vec3, BCMath.PI_BY_TWO);
    }
}
