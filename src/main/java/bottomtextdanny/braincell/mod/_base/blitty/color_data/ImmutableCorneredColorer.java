package bottomtextdanny.braincell.mod._base.blitty.color_data;

import net.minecraft.util.Mth;

public record ImmutableCorneredColorer(BlittyColorOperation operation, int topLeftRed, int topLeftGreen, int topLeftBlue, int topLeftAlpha, int topRightRed, int topRightGreen, int topRightBlue, int topRightAlpha, int bottomLeftRed, int bottomLeftGreen, int bottomLeftBlue, int bottomLeftAlpha, int bottomRightRed, int bottomRightGreen, int bottomRightBlue, int bottomRightAlpha) implements BlittyColorTransformer {
   public ImmutableCorneredColorer(BlittyColorOperation operation, int topLeftRed, int topLeftGreen, int topLeftBlue, int topLeftAlpha, int topRightRed, int topRightGreen, int topRightBlue, int topRightAlpha, int bottomLeftRed, int bottomLeftGreen, int bottomLeftBlue, int bottomLeftAlpha, int bottomRightRed, int bottomRightGreen, int bottomRightBlue, int bottomRightAlpha) {
      this.operation = operation;
      this.topLeftRed = topLeftRed;
      this.topLeftGreen = topLeftGreen;
      this.topLeftBlue = topLeftBlue;
      this.topLeftAlpha = topLeftAlpha;
      this.topRightRed = topRightRed;
      this.topRightGreen = topRightGreen;
      this.topRightBlue = topRightBlue;
      this.topRightAlpha = topRightAlpha;
      this.bottomLeftRed = bottomLeftRed;
      this.bottomLeftGreen = bottomLeftGreen;
      this.bottomLeftBlue = bottomLeftBlue;
      this.bottomLeftAlpha = bottomLeftAlpha;
      this.bottomRightRed = bottomRightRed;
      this.bottomRightGreen = bottomRightGreen;
      this.bottomRightBlue = bottomRightBlue;
      this.bottomRightAlpha = bottomRightAlpha;
   }

   public void transform(BlittyColor color) {
      color.r = (Float)this.operation.func.apply(color.r, Mth.lerp(color.transitionY, Mth.lerp(color.transitionX, (float)this.topLeftRed, (float)this.topRightRed), Mth.lerp(color.transitionX, (float)this.bottomLeftRed, (float)this.bottomRightRed)));
      color.g = (Float)this.operation.func.apply(color.g, Mth.lerp(color.transitionY, Mth.lerp(color.transitionX, (float)this.topLeftGreen, (float)this.topRightGreen), Mth.lerp(color.transitionX, (float)this.bottomLeftGreen, (float)this.bottomRightGreen)));
      color.b = (Float)this.operation.func.apply(color.b, Mth.lerp(color.transitionY, Mth.lerp(color.transitionX, (float)this.topLeftBlue, (float)this.topRightBlue), Mth.lerp(color.transitionX, (float)this.bottomLeftBlue, (float)this.bottomRightBlue)));
      color.b = (Float)this.operation.func.apply(color.b, Mth.lerp(color.transitionY, Mth.lerp(color.transitionX, (float)this.topLeftAlpha, (float)this.topRightAlpha), Mth.lerp(color.transitionX, (float)this.bottomLeftAlpha, (float)this.bottomRightAlpha)));
   }

   public BlittyColorOperation operation() {
      return this.operation;
   }

   public int topLeftRed() {
      return this.topLeftRed;
   }

   public int topLeftGreen() {
      return this.topLeftGreen;
   }

   public int topLeftBlue() {
      return this.topLeftBlue;
   }

   public int topLeftAlpha() {
      return this.topLeftAlpha;
   }

   public int topRightRed() {
      return this.topRightRed;
   }

   public int topRightGreen() {
      return this.topRightGreen;
   }

   public int topRightBlue() {
      return this.topRightBlue;
   }

   public int topRightAlpha() {
      return this.topRightAlpha;
   }

   public int bottomLeftRed() {
      return this.bottomLeftRed;
   }

   public int bottomLeftGreen() {
      return this.bottomLeftGreen;
   }

   public int bottomLeftBlue() {
      return this.bottomLeftBlue;
   }

   public int bottomLeftAlpha() {
      return this.bottomLeftAlpha;
   }

   public int bottomRightRed() {
      return this.bottomRightRed;
   }

   public int bottomRightGreen() {
      return this.bottomRightGreen;
   }

   public int bottomRightBlue() {
      return this.bottomRightBlue;
   }

   public int bottomRightAlpha() {
      return this.bottomRightAlpha;
   }
}
