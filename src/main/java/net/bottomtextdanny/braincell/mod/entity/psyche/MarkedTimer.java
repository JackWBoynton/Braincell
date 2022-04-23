package net.bottomtextdanny.braincell.mod.entity.psyche;

import net.bottomtextdanny.braincell.base.scheduler.IntScheduler;

public class MarkedTimer {
    public final IntScheduler timer;
    private Object usageTicket;

    public MarkedTimer(IntScheduler timer) {
        super();
        this.timer = timer;
    }

    public void mark(Object object) {
        this.usageTicket = object;
    }

    public boolean testMark(Object object) {
        return this.usageTicket == object;
    }
}
