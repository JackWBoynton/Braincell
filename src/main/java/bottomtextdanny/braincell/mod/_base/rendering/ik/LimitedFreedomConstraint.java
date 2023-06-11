package bottomtextdanny.braincell.mod._base.rendering.ik;

import bottomtextdanny.braincell.base.Axis2D;
import java.util.Objects;
import javax.annotation.Nullable;

public final class LimitedFreedomConstraint implements IKConstraint {
   private final Axis2D axis;
   private final float clampedFrom;
   private final float clampedTo;

   public LimitedFreedomConstraint(Axis2D axis, float degreeClampedFrom, float degreeClampedTo) {
      this.axis = axis;
      this.clampedFrom = degreeClampedFrom * 0.017453292F;
      this.clampedTo = degreeClampedTo * 0.017453292F;
   }

   public void applyToSection(IKPartData parentData, IKPartData data, @Nullable IKPartData childData) {
      float unclamped = data.getAngleByAxis(this.axis);
      if (unclamped < this.clampedFrom) {
         data.setAngleByAxis(this.axis, this.clampedFrom);
      } else if (unclamped > this.clampedTo) {
         data.setAngleByAxis(this.axis, this.clampedTo);
      }

   }

   public Axis2D axis() {
      return this.axis;
   }

   public float clampedFrom() {
      return this.clampedFrom;
   }

   public float clampedTo() {
      return this.clampedTo;
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (obj != null && obj.getClass() == this.getClass()) {
         LimitedFreedomConstraint that = (LimitedFreedomConstraint)obj;
         return Objects.equals(this.axis, that.axis) && Float.floatToIntBits(this.clampedFrom) == Float.floatToIntBits(that.clampedFrom) && Float.floatToIntBits(this.clampedTo) == Float.floatToIntBits(that.clampedTo);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.axis, this.clampedFrom, this.clampedTo});
   }

   public String toString() {
      return "LimitedFreedomConstraint[axis=" + this.axis + ", clampedFrom=" + this.clampedFrom + ", clampedTo=" + this.clampedTo + "]";
   }
}
