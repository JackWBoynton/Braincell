package bottomtextdanny.braincell.mod.capability.player.accessory.extensions;

public class CriticalStateMutable {
   private ForceCriticalDirectValue state;

   public CriticalStateMutable() {
      this.state = ForceCriticalDirectValue.NOT_HAPPEN;
   }

   public void setState(ForceCriticalDirectValue newState) {
      if (newState == null) {
         throw new UnsupportedOperationException("no");
      } else {
         if (this.state.ordinal() > newState.ordinal()) {
            this.state = newState;
         }

      }
   }

   public ForceCriticalDirectValue getState() {
      return this.state;
   }
}
