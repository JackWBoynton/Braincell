package bottomtextdanny.braincell.mod._base.rendering.ik;

import bottomtextdanny.braincell.base.Axis2D;

public class IKPartData {
    public float x, y, z, xRot, yRot;
    public float initialized;

    public float getAngleByAxis(Axis2D axis) {
        if (axis == Axis2D.X) {
            return this.xRot;
        } else {
            return this.yRot;
        }
    }

    public void setAngleByAxis(Axis2D axis, float radValue) {
        if (axis == Axis2D.X) {
            this.xRot = radValue;
        } else {
            this.yRot = radValue;
        }
    }
}
