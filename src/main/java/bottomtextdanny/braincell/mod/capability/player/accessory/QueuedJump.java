package bottomtextdanny.braincell.mod.capability.player.accessory;

public class QueuedJump implements IQueuedJump {
   private boolean used;
   private final JumpPriority priority;
   private final IJumpQueuerAccessory jumpProvider;

   public QueuedJump(IJumpQueuerAccessory jumpProvider, JumpPriority priority) {
      this.jumpProvider = jumpProvider;
      this.priority = priority;
   }

   public JumpPriority priority() {
      return this.priority;
   }

   public boolean canPerform() {
      return this.getAccessory().canPerformJump(this);
   }

   public void performJump() {
      this.jumpProvider.performJump(this);
   }

   public void reestablish() {
      this.used = false;
   }

   public void use() {
      this.used = true;
   }

   public boolean isUsed() {
      return this.used;
   }

   public IJumpQueuerAccessory getAccessory() {
      return this.jumpProvider;
   }
}
