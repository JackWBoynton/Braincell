/*
 * Copyright Monday August 15 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.model_animation.ik;

import bottomtextdanny.braincell.libraries.model.BCJoint;

import java.util.function.Function;

public class CCDIKLinkBuilder<T> {
	private BCJoint joint;
	private Function<T, CCDIKPartData> dataGetter;
	private CCDIKConstraint constraint;
	private CCDIKAxisConstraint xConstraint;
	private CCDIKAxisConstraint yConstraint;
	private CCDIKAxisConstraint zConstraint;

	public CCDIKLinkBuilder setJoint(BCJoint joint) {
		this.joint = joint;
		return this;
	}

	public CCDIKLinkBuilder setDataGetter(Function<T, CCDIKPartData> dataGetter) {
		this.dataGetter = dataGetter;
		return this;
	}

	public CCDIKLinkBuilder setConstraint(CCDIKConstraint constraint) {
		this.constraint = constraint;
		return this;
	}

	public CCDIKLinkBuilder setxConstraint(CCDIKAxisConstraint xConstraint) {
		this.xConstraint = xConstraint;
		return this;
	}

	public CCDIKLinkBuilder setyConstraint(CCDIKAxisConstraint yConstraint) {
		this.yConstraint = yConstraint;
		return this;
	}

	public CCDIKLinkBuilder setzConstraint(CCDIKAxisConstraint zConstraint) {
		this.zConstraint = zConstraint;
		return this;
	}

	public CCDIKLink createCCDIKLink() {
		return new CCDIKLink(joint, dataGetter, constraint, xConstraint, yConstraint, zConstraint);
	}
}