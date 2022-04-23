package net.bottomtextdanny.braincell.mod.capability.player.accessory;

public interface IJumpQueuerAccessory extends IAccessory {

    boolean canPerformJump(IQueuedJump jumpType);

    void performJump(IQueuedJump jumpType);

    IQueuedJump[] provideJumps();
}
