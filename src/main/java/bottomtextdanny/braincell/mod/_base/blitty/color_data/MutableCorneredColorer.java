package bottomtextdanny.braincell.mod._base.blitty.color_data;

import java.util.Objects;
import net.minecraft.util.Mth;

public final class MutableCorneredColorer implements BlittyColorTransformer {
   private final BlittyColorOperation operation;
   private float topLeftRed;
   private float topLeftGreen;
   private float topLeftBlue;
   private float topLeftAlpha;
   private float topRightRed;
   private float topRightGreen;
   private float topRightBlue;
   private float topRightAlpha;
   private float bottomLeftRed;
   private float bottomLeftGreen;
   private float bottomLeftBlue;
   private float bottomLeftAlpha;
   private float bottomRightRed;
   private float bottomRightGreen;
   private float bottomRightBlue;
   private float bottomRightAlpha;

   public MutableCorneredColorer(BlittyColorOperation operation, float topLeftRed, float topLeftGreen, float topLeftBlue, float topLeftAlpha, float topRightRed, float topRightGreen, float topRightBlue, float topRightAlpha, float bottomLeftRed, float bottomLeftGreen, float bottomLeftBlue, float bottomLeftAlpha, float bottomRightRed, float bottomRightGreen, float bottomRightBlue, float bottomRightAlpha) {
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
      color.r = (Float)this.operation.func.apply(color.r, Mth.lerp(color.transitionY, Mth.lerp(color.transitionX, this.topLeftRed, this.topRightRed), Mth.lerp(color.transitionX, this.bottomLeftRed, this.bottomRightRed)));
      color.g = (Float)this.operation.func.apply(color.g, Mth.lerp(color.transitionY, Mth.lerp(color.transitionX, this.topLeftGreen, this.topRightGreen), Mth.lerp(color.transitionX, this.bottomLeftGreen, this.bottomRightGreen)));
      color.b = (Float)this.operation.func.apply(color.b, Mth.lerp(color.transitionY, Mth.lerp(color.transitionX, this.topLeftBlue, this.topRightBlue), Mth.lerp(color.transitionX, this.bottomLeftBlue, this.bottomRightBlue)));
      color.b = (Float)this.operation.func.apply(color.b, Mth.lerp(color.transitionY, Mth.lerp(color.transitionX, this.topLeftAlpha, this.topRightAlpha), Mth.lerp(color.transitionX, this.bottomLeftAlpha, this.bottomRightAlpha)));
   }

   public void setTopLeft(float red, float green, float blue, float alpha) {
      this.topLeftRed = red;
      this.topLeftGreen = green;
      this.topLeftBlue = blue;
      this.topLeftAlpha = alpha;
   }

   public void setTopRight(float red, float green, float blue, float alpha) {
      this.topRightRed = red;
      this.topRightGreen = green;
      this.topRightBlue = blue;
      this.topRightAlpha = alpha;
   }

   public void setBottomLeft(float red, float green, float blue, float alpha) {
      this.bottomLeftRed = red;
      this.bottomLeftGreen = green;
      this.bottomLeftBlue = blue;
      this.bottomLeftAlpha = alpha;
   }

   public void setBottomRight(float red, float green, float blue, float alpha) {
      this.bottomRightRed = red;
      this.bottomRightGreen = green;
      this.bottomRightBlue = blue;
      this.bottomRightAlpha = alpha;
   }

   public float topLeftRed() {
      return this.topLeftRed;
   }

   public float topLeftGreen() {
      return this.topLeftGreen;
   }

   public float topLeftBlue() {
      return this.topLeftBlue;
   }

   public float topLeftAlpha() {
      return this.topLeftAlpha;
   }

   public float topRightRed() {
      return this.topRightRed;
   }

   public float topRightGreen() {
      return this.topRightGreen;
   }

   public float topRightBlue() {
      return this.topRightBlue;
   }

   public float topRightAlpha() {
      return this.topRightAlpha;
   }

   public float bottomLeftRed() {
      return this.bottomLeftRed;
   }

   public float bottomLeftGreen() {
      return this.bottomLeftGreen;
   }

   public float bottomLeftBlue() {
      return this.bottomLeftBlue;
   }

   public float bottomLeftAlpha() {
      return this.bottomLeftAlpha;
   }

   public float bottomRightRed() {
      return this.bottomRightRed;
   }

   public float bottomRightGreen() {
      return this.bottomRightGreen;
   }

   public float bottomRightBlue() {
      return this.bottomRightBlue;
   }

   public float bottomRightAlpha() {
      return this.bottomRightAlpha;
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (obj != null && obj.getClass() == this.getClass()) {
         MutableCorneredColorer that = (MutableCorneredColorer)obj;
         return this.topLeftRed == that.topLeftRed && this.topLeftGreen == that.topLeftGreen && this.topLeftBlue == that.topLeftBlue && this.topLeftAlpha == that.topLeftAlpha && this.topRightRed == that.topRightRed && this.topRightGreen == that.topRightGreen && this.topRightBlue == that.topRightBlue && this.topRightAlpha == that.topRightAlpha && this.bottomLeftRed == that.bottomLeftRed && this.bottomLeftGreen == that.bottomLeftGreen && this.bottomLeftBlue == that.bottomLeftBlue && this.bottomLeftAlpha == that.bottomLeftAlpha && this.bottomRightRed == that.bottomRightRed && this.bottomRightGreen == that.bottomRightGreen && this.bottomRightBlue == that.bottomRightBlue && this.bottomRightAlpha == that.bottomRightAlpha;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.topLeftRed, this.topLeftGreen, this.topLeftBlue, this.topLeftAlpha, this.topRightRed, this.topRightGreen, this.topRightBlue, this.topRightAlpha, this.bottomLeftRed, this.bottomLeftGreen, this.bottomLeftBlue, this.bottomLeftAlpha, this.bottomRightRed, this.bottomRightGreen, this.bottomRightBlue, this.bottomRightAlpha});
   }

   public String toString() {
      return "MutableColorData[topLeftRed=" + this.topLeftRed + ", topLeftGreen=" + this.topLeftGreen + ", topLeftBlue=" + this.topLeftBlue + ", topLeftAlpha=" + this.topLeftAlpha + ", topRightRed=" + this.topRightRed + ", topRightGreen=" + this.topRightGreen + ", topRightBlue=" + this.topRightBlue + ", topRightAlpha=" + this.topRightAlpha + ", bottomLeftRed=" + this.bottomLeftRed + ", bottomLeftGreen=" + this.bottomLeftGreen + ", bottomLeftBlue=" + this.bottomLeftBlue + ", bottomLeftAlpha=" + this.bottomLeftAlpha + ", bottomRightRed=" + this.bottomRightRed + ", bottomRightGreen=" + this.bottomRightGreen + ", bottomRightBlue=" + this.bottomRightBlue + ", bottomRightAlpha=" + this.bottomRightAlpha + "]";
   }
}
