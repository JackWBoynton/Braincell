package bottomtextdanny.braincell.mod.capability.player.accessory;

public interface IJumpQueuerAccessory extends IAccessory {
   boolean canPerformJump(IQueuedJump var1);

   void performJump(IQueuedJump var1);

   IQueuedJump[] provideJumps();
}
