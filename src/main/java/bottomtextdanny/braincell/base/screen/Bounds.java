package bottomtextdanny.braincell.base.screen;

public record Bounds(int width, int height) implements ImageBounds {
   public Bounds(int width, int height) {
      this.width = width;
      this.height = height;
   }

   public int width() {
      return this.width;
   }

   public int height() {
      return this.height;
   }
}
