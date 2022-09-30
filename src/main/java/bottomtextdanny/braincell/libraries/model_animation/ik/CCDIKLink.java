/*
 * Copyright Friday August 05 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.model_animation.ik;

import bottomtextdanny.braincell.base.Easing;
import bottomtextdanny.braincell.libraries.model.BCJoint;
import bottomtextdanny.braincell.libraries.model.BCModel;
import bottomtextdanny.braincell.libraries.model.ModelSectionReseter;
import bottomtextdanny.braincell.libraries.model_animation.AnimatableModelComponent;
import bottomtextdanny.braincell.libraries.model_animation.RotMutator;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;

public final class CCDIKLink<T> implements ModelSectionReseter, AnimatableModelComponent<RotMutator> {
    private final BCJoint joint;
    private final Function<T, CCDIKPartData> dataGetter;
    public final CCDIKConstraint constraint;
    public final CCDIKAxisConstraint xConstraint;
    public final CCDIKAxisConstraint yConstraint;
    public final CCDIKAxisConstraint zConstraint;
    public float xRot;
    public float yRot;
    public float zRot;

    CCDIKLink(BCJoint joint, Function<T, CCDIKPartData> dataGetter,
              @Nullable CCDIKConstraint constraint, @Nullable CCDIKAxisConstraint xConstraint,
              @Nullable CCDIKAxisConstraint yConstraint, @Nullable CCDIKAxisConstraint zConstraint) {
        this.joint = joint;
        this.dataGetter = dataGetter;
        if (constraint == null) constraint = CCDIKConstraint.NO_CONSTRAINT;
        if (xConstraint == null) xConstraint = CCDIKAxisConstraint.NO_CONSTRAINT;
        if (yConstraint == null) yConstraint = CCDIKAxisConstraint.NO_CONSTRAINT;
        if (zConstraint == null) zConstraint = CCDIKAxisConstraint.NO_CONSTRAINT;
        this.constraint = constraint;
        this.xConstraint = xConstraint;
        this.yConstraint = yConstraint;
        this.zConstraint = zConstraint;
    }

    public static <T> Builder<T> builder(BCJoint joint, Function<T, CCDIKPartData> dataGetter) {
        return new Builder<>(joint, dataGetter);
    }

    public CCDIKConstraint getConstraint() {
        return constraint;
    }

    public BCJoint joint() {
        return joint;
    }

    public Function<T, CCDIKPartData> dataGetter() {
        return dataGetter;
    }

    public void setX(float value) {
        xRot = value;
    }

    public void setY(float value) {
        yRot = value;
    }

    public void setZ(float value) {
        zRot = value;
    }

    public void addX(float value) {
        xRot += value;
    }

    public void addY(float value) {
        yRot += value;
    }

    public void addZ(float value) {
        zRot += value;
    }

    @Override
    public String toString() {
        return "BCIKPart[" +
                "joint=" + joint + ", " +
                "dataGetter=" + dataGetter + ", " +
                "constraints=" + constraint + ']';
    }

    @Override
    public void reset(BCModel model) {
        xRot = 0.0F;
        yRot = 0.0F;
        zRot = 0.0F;
    }

    @Override
    public String name() {
        return "ccdiklink";
    }

    @Override
    public RotMutator newAnimationData() {
        return new RotMutator();
    }

    @Override
    public void animationTransitionerPrevious(RotMutator previous, RotMutator current, float multiplier, float progression, Map<Easing, Float> easingMap) {
        progression = 1.0F - progression;
        xRot += progression * previous.getRotX();
        yRot += progression * previous.getRotY();
        zRot += progression * previous.getRotZ();
    }

    @Override
    public void animationTransitionerCurrent(RotMutator current, float multiplier, float progression, Map<Easing, Float> easingMap) {
        xRot += progression * current.getRotX();
        yRot += progression * current.getRotY();
        zRot += progression * current.getRotZ();
    }

    public static class Builder<T> {
        private final BCJoint joint;
        private final Function<T, CCDIKPartData> dataGetter;
        private CCDIKConstraint constraint;
        private CCDIKAxisConstraint xConstraint;
        private CCDIKAxisConstraint yConstraint;
        private CCDIKAxisConstraint zConstraint;

        public Builder(BCJoint joint, Function<T, CCDIKPartData> dataGetter) {
            this.joint = joint;
            this.dataGetter = dataGetter;
        }

        public Builder<T> finalConstraint(CCDIKConstraint constraint) {
            this.constraint = constraint;
            return this;
        }

        public Builder<T> finalConstraint(CCDIKConstraint... constraints) {
            CCDIKConstraint constraint = constraints[0];
            for (int i = 1; i < constraints.length; i++) {
                constraint = constraint.append(constraints[i]);
            }
            this.constraint = constraint;
            return this;
        }

        public Builder<T> xConstraint(CCDIKAxisConstraint xConstraint) {
            this.xConstraint = xConstraint;
            return this;
        }

        public Builder<T> xConstraint(CCDIKAxisConstraint... constraints) {
            CCDIKAxisConstraint constraint = constraints[0];
            for (int i = 1; i < constraints.length; i++) {
                constraint = constraint.append(constraints[i]);
            }
            this.xConstraint = constraint;
            return this;
        }

        public Builder<T> yConstraint(CCDIKAxisConstraint constraint) {
            this.yConstraint = constraint;
            return this;
        }

        public Builder<T> yConstraint(CCDIKAxisConstraint... constraints) {
            CCDIKAxisConstraint constraint = constraints[0];
            for (int i = 1; i < constraints.length; i++) {
                constraint = constraint.append(constraints[i]);
            }
            this.yConstraint = constraint;
            return this;
        }

        public Builder<T> zConstraint(CCDIKAxisConstraint constraint) {
            this.zConstraint = constraint;
            return this;
        }

        public Builder<T> zConstraint(CCDIKAxisConstraint... constraints) {
            CCDIKAxisConstraint constraint = constraints[0];
            for (int i = 1; i < constraints.length; i++) {
                constraint = constraint.append(constraints[i]);
            }
            this.zConstraint = constraint;
            return this;
        }

        public CCDIKLink<T> createCCDIKLink() {
            return new CCDIKLink<>(joint, dataGetter, constraint, xConstraint, yConstraint, zConstraint);
        }
    }
}
