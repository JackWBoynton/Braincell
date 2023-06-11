package bottomtextdanny.braincell.mod._base;

public abstract class AbstractModSide {
   protected final String modId;

   public AbstractModSide(String modId) {
      this.modId = modId;
   }

   public abstract void modLoadingCallOut();

   public abstract void postModLoadingPhaseCallOut();
}
