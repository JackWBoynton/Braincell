/*
 * Copyright Saturday August 06 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.model_animation.ik;

import bottomtextdanny.braincell.base.Axis2D;
import org.apache.commons.lang3.mutable.MutableFloat;

public class CCDIKPartData {
    public float xRot, yRot, zRot;

    public float getAngleByAxis(Axis2D axis) {
        if (axis == Axis2D.X) {
            return this.xRot;
        } else {
            return this.yRot;
        }
    }

    public float getAngleByAxis(Axis2D axis, MutableFloat xDelta, MutableFloat yDelta) {
        if (axis == Axis2D.X) {
            return this.xRot + xDelta.floatValue();
        } else {
            return this.yRot + yDelta.floatValue();
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
