package net.bottomtextdanny.braincell.mod.entity.psyche;

import net.bottomtextdanny.braincell.base.scheduler.IntScheduler;

import javax.annotation.Nullable;

public class MarkedTimer {
    public final IntScheduler timer;
    @Nullable
    private Object usageTicket;

    public MarkedTimer(IntScheduler timer) {
        super();
        this.timer = timer;
    }

    public void setMarkedBy(Object object) {
        this.usageTicket = object;
    }

    public void unmark() {
        this.usageTicket = null;
    }

    public boolean isMarkedBy(Object object) {
        return this.usageTicket == object;
    }

    public boolean isUnmarkedOrMarkedBy(Object object) {
        return this.usageTicket == object || this.usageTicket == null;
    }

    public boolean isUnmarked() {
        return this.usageTicket == null;
    }
}
