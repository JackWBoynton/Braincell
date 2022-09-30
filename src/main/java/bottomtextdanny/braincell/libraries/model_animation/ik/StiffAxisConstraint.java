/*
 * Copyright Saturday August 06 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.model_animation.ik;

import bottomtextdanny.braincell.base.Axis2D;
import bottomtextdanny.braincell.base.BCMath;
import bottomtextdanny.braincell.base.Easing;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.mutable.MutableFloat;

public record StiffAxisConstraint(Axis2D axis, float anchor, float negRadius, float posRadius) implements CCDIKConstraint {

    @Override
    public void apply(CCDIKPartData data, float xDelta, float yDelta, float zDelta, float xRotO, float yRotO, float zRotO) {

    }

//    @Override
//    public void applyToJoint(CCDIKPartData data, MutableFloat xDif, MutableFloat yDif) {
//        float v, unclamped;
//        if (axis == Axis2D.X) {
//            v = xDif.floatValue();
//            unclamped = data.xRot + v;
//            if (unclamped < anchor) {
//                if (unclamped > negRadius)
//                    xDif.setValue(v * (1 - BCMath.radianAngleDiff(anchor, unclamped) / (anchor - negRadius)));
//                else xDif.setValue(BCMath.radianAngleDiff(negRadius, data.xRot));
//            } else if (unclamped > anchor) {
//               // Minecraft.getInstance().player.chat(String.valueOf(unclamped));
//                if (unclamped < posRadius)
//                    xDif.setValue(v * (1 - BCMath.radianAngleDiff(anchor, unclamped) / (posRadius - anchor)));
//                else xDif.setValue(BCMath.radianAngleDiff(posRadius, data.xRot));
//            }
//        } else {
//            v = yDif.floatValue();
//            unclamped = data.yRot + v;
//            if (unclamped < anchor) {
//                if (unclamped > negRadius)
//                    yDif.setValue(v * (1 - BCMath.radianAngleDiff(anchor, unclamped) / (anchor - negRadius)));
//                else yDif.setValue(BCMath.radianAngleDiff(negRadius, data.yRot));
//            } else if (unclamped > anchor) {
//                if (unclamped < posRadius)
//                    yDif.setValue(v * (1 - BCMath.radianAngleDiff(anchor, unclamped) / (posRadius - anchor)));
//                else yDif.setValue(BCMath.radianAngleDiff(posRadius, data.yRot));
//            }
//        }
//    }
}
