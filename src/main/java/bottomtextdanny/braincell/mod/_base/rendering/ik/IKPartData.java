package bottomtextdanny.braincell.mod._base.rendering.ik;

import bottomtextdanny.braincell.base.Axis2D;

public class IKPartData {
   public float x;
   public float y;
   public float z;
   public float xRot;
   public float yRot;
   public float initialized;

   public float getAngleByAxis(Axis2D axis) {
      return axis == Axis2D.X ? this.xRot : this.yRot;
   }

   public void setAngleByAxis(Axis2D axis, float radValue) {
      if (axis == Axis2D.X) {
         this.xRot = radValue;
      } else {
         this.yRot = radValue;
      }

   }
}
