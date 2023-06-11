package bottomtextdanny.braincell.mod._base;

public interface Module {
   void activate();

   boolean isActive();

   static Module makeSimple(SideConfig sideConfiguration) {
      return new Simple(sideConfiguration);
   }

   public static class Simple implements Module {
      private boolean activated;
      private final SideConfig side;

      private Simple(SideConfig side) {
         this.side = side;
      }

      public void activate() {
         if (this.side.test()) {
            this.activated = true;
         }

      }

      public boolean isActive() {
         return this.activated;
      }
   }
}
