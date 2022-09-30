/*
 * Copyright Saturday August 06 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.model_animation.ik;

import bottomtextdanny.braincell.base.Axis2D;
import bottomtextdanny.braincell.base.Easing;
import net.minecraft.core.Direction;

public record BiasConstraint(Direction.Axis axis, float anchor, float mulStep) implements CCDIKAxisConstraint {

    @Override
    public float apply(float rot, float delta, float rotO) {
        return rot + (anchor - rot) * mulStep;
    }

//    @Override
//    public void applyToJoint(CCDIKPartData data, MutableFloat xDif, MutableFloat yDif) {
//        float v, unclamped;
//        if (axis == Axis2D.X) {
//            v = xDif.floatValue();
//            unclamped = data.xRot + v;
//            if (unclamped < anchor) {
//                if (unclamped > negRadius) {
//                    xDif.setValue(v * easing.progression(1 - BCMath.radianAngleDiff(anchor, data.xRot) / (anchor - negRadius)));
//                }
//                else {
//                    xDif.setValue(BCMath.radianAngleDiff(negRadius, data.xRot));
//                }
//            } else if (unclamped > anchor) {
//                if (unclamped < posRadius) {
//                   // Minecraft.getInstance().player.chat(String.valueOf(BCMath.radianAngleDiff(anchor, data.xRot) / (anchor - posRadius)));
//                    xDif.setValue(v * easing.progression(1 - BCMath.radianAngleDiff(anchor, data.xRot) / (posRadius - anchor)));
//                }
//                else {
//                   // Minecraft.getInstance().player.chat(String.valueOf(unclamped));
//                    xDif.setValue(BCMath.radianAngleDiff(posRadius, data.xRot));
//                }
//            }
//        } else {
//            v = yDif.floatValue();
//            unclamped = data.yRot + v;
//            if (unclamped < anchor) {
//                if (unclamped > negRadius)
//                    yDif.setValue(v * easing.progression(1 - BCMath.radianAngleDiff(anchor, unclamped) / (anchor - negRadius)));
//                else yDif.setValue(BCMath.radianAngleDiff(negRadius, data.yRot));
//            } else if (unclamped > anchor) {
//                if (unclamped < posRadius)
//                    yDif.setValue(v * easing.progression(1 - BCMath.radianAngleDiff(anchor, unclamped) / (posRadius - anchor)));
//                else yDif.setValue(BCMath.radianAngleDiff(posRadius, data.yRot));
//            }
//        }
//    }
}
