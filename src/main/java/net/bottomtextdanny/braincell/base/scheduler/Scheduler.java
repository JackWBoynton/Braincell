package net.bottomtextdanny.braincell.base.scheduler;

public interface Scheduler<T> {

    void reset();

    void end();

    void advance();

    T current();

    T bound();

    boolean hasEnded();
}
