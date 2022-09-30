/*
 * Copyright Saturday August 06 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.model_animation.ik;

import bottomtextdanny.braincell.base.Axis2D;
import bottomtextdanny.braincell.base.BCMath;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import org.apache.commons.lang3.mutable.MutableFloat;

import static java.lang.Math.toDegrees;

public record LimitedAxisConstraint(float clampedFrom, float clampedTo) implements CCDIKAxisConstraint {

	@Override
	public float apply(float rot, float delta, float rotO) {
		if (rot > clampedTo) {
			return Math.min(rotO, clampedTo);
		} else if (rot < clampedFrom) {
			return Math.max(rotO, clampedFrom);
		}
		return rot;
	}

//	@Override
//    public void applyToJoint(CCDIKPartData data, MutableFloat xDif, MutableFloat yDif) {
//        float v, unclamped;
//        if (axis == Axis2D.X) {
//            v = xDif.floatValue();
//            unclamped = data.xRot + v;
//            if (unclamped < this.clampedFrom) {
//				if (v > 0) {
//					xDif.setValue(BCMath.radianAngleDiff(clampedFrom, data.xRot));
//				}
//				Minecraft.getInstance().player.chat(data.xRot + " clampedFrom " + clampedFrom);
//            } else if (unclamped > this.clampedTo) {
//				if (v < 0) {
//					xDif.setValue(BCMath.radianAngleDiff(clampedTo, data.xRot));
//				}
//				Minecraft.getInstance().player.chat(data.xRot + " clampedTo " + clampedTo);
//            }
//        } else {
//            v = yDif.floatValue();
//            unclamped = data.yRot + v;
//            if (unclamped < this.clampedFrom) {
//                yDif.setValue(BCMath.radianAngleDiff(clampedFrom, data.yRot));
//            } else if (unclamped > this.clampedTo) {
//                yDif.setValue(BCMath.radianAngleDiff(clampedTo, data.yRot));
//            }
//        }
//    }
}
