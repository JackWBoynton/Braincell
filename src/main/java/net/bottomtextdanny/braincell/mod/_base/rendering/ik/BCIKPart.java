package net.bottomtextdanny.braincell.mod._base.rendering.ik;

import net.bottomtextdanny.braincell.mod._base.rendering.core_modeling.BCJoint;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Function;

public final class BCIKPart<T> {
    private final BCJoint joint;
    private final Function<T, IKPartData> dataGetter;
    private @Nullable IKConstraint constraint;

    public BCIKPart(BCJoint joint, Function<T, IKPartData> dataGetter, IKConstraint... constraints) {
        this.joint = joint;
        this.dataGetter = dataGetter;
        for (IKConstraint constraint : constraints) {
            if (this.constraint == null) {
                this.constraint = constraint;
            } else {
                this.constraint = this.constraint.andThen(constraint);
            }
        }
    }

    @Nullable
    public IKConstraint getConstraint() {
        return constraint;
    }

    public BCJoint joint() {
        return joint;
    }

    public Function<T, IKPartData> dataGetter() {
        return dataGetter;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (BCIKPart) obj;
        return Objects.equals(this.joint, that.joint) &&
                Objects.equals(this.dataGetter, that.dataGetter) &&
                Objects.equals(this.constraint, that.constraint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(joint, dataGetter, constraint);
    }

    @Override
    public String toString() {
        return "BCIKPart[" +
                "joint=" + joint + ", " +
                "dataGetter=" + dataGetter + ", " +
                "constraints=" + constraint + ']';
    }
}
