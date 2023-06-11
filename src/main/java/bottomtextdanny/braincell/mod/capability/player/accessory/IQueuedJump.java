package bottomtextdanny.braincell.mod.capability.player.accessory;

public interface IQueuedJump {
   boolean canPerform();

   void performJump();

   void reestablish();

   JumpPriority priority();

   IJumpQueuerAccessory getAccessory();
}
