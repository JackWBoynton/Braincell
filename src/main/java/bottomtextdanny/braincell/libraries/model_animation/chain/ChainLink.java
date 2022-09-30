/*
 * Copyright Thursday August 18 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.model_animation.chain;

import bottomtextdanny.braincell.libraries.model.BCJoint;
import bottomtextdanny.braincell.libraries.model_animation.ik.CCDIKPartData;

import java.util.function.Function;

public class ChainLink<T> {
	public final BCJoint joint;
	public final Function<T, CCDIKPartData> dataGetter;
	public float xo, yo, zo, x, y, z, xRot;

	public ChainLink(BCJoint joint, Function<T, CCDIKPartData> dataGetter) {
		this.joint = joint;
		this.dataGetter = dataGetter;
	}
}
