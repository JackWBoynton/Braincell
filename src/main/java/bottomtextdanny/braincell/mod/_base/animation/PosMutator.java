package bottomtextdanny.braincell.mod._base.animation;

public class PosMutator {
   private float posX;
   private float posY;
   private float posZ;

   public void setPos(float posX, float posY, float posZ) {
      this.posX = posX;
      this.posY = posY;
      this.posZ = posZ;
   }

   public float getPosX() {
      return this.posX;
   }

   public float getPosY() {
      return this.posY;
   }

   public float getPosZ() {
      return this.posZ;
   }
}
