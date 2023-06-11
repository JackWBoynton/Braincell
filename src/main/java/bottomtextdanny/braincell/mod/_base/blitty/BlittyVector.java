package bottomtextdanny.braincell.mod._base.blitty;

public enum BlittyVector {
   UP(0, 1),
   RIGHT(1, 0),
   DOWN(0, -1),
   LEFT(-1, 0);

   public final int x;
   public final int y;
   private final boolean usesX;
   private final boolean usesY;

   private BlittyVector(int x, int y) {
      this.x = x;
      this.y = y;
      this.usesX = this.x != 0;
      this.usesY = this.y != 0;
   }

   public boolean usesX() {
      return this.usesX;
   }

   public boolean usesY() {
      return this.usesY;
   }

   // $FF: synthetic method
   private static BlittyVector[] $values() {
      return new BlittyVector[]{UP, RIGHT, DOWN, LEFT};
   }
}
