package bottomtextdanny.braincell.mod._base.rendering.ik;

import bottomtextdanny.braincell.base.Axis2D;
import javax.annotation.Nullable;

public record LockedAxisConstraint(Axis2D axis) implements IKConstraint {
   public static final LockedAxisConstraint X;
   public static final LockedAxisConstraint Y;

   public LockedAxisConstraint(Axis2D axis) {
      this.axis = axis;
   }

   public void applyToSection(IKPartData parentData, IKPartData data, @Nullable IKPartData childData) {
      data.setAngleByAxis(this.axis, 0.0F);
   }

   public Axis2D axis() {
      return this.axis;
   }

   static {
      X = new LockedAxisConstraint(Axis2D.X);
      Y = new LockedAxisConstraint(Axis2D.Y);
   }
}
