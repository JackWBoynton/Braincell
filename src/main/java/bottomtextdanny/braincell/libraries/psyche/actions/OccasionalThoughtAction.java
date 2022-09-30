package bottomtextdanny.braincell.libraries.psyche.actions;

import bottomtextdanny.braincell.base.scheduler.IntScheduler;
import bottomtextdanny.braincell.libraries.psyche.Action;
import net.minecraft.world.entity.PathfinderMob;

public abstract class OccasionalThoughtAction<E extends PathfinderMob> extends Action<E> {
    protected IntScheduler thoughtSchedule;
    private boolean updateTime;

    public OccasionalThoughtAction(E mob, IntScheduler timedSchedule) {
        super(mob);
        this.thoughtSchedule = timedSchedule;
    }

    public final void update() {
        this.updateTime = false;
        this.thoughtSchedule.incrementFreely(1);
        if (this.thoughtSchedule.hasEnded()) {
            thoughtAction(this.thoughtSchedule.current());
            this.thoughtSchedule.reset();
            this.updateTime = true;
        }
    }

    public abstract void thoughtAction(int timeSinceBefore);

    public boolean isUpdateTime() {
        return this.updateTime;
    }

    @Override
    public boolean shouldKeepGoing() {
        return active();
    }
}
