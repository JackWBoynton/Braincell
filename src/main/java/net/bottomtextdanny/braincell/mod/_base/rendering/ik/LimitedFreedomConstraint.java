package net.bottomtextdanny.braincell.mod._base.rendering.ik;

import net.bottomtextdanny.braincell.base.Axis2D;
import net.bottomtextdanny.braincell.base.BCMath;

import javax.annotation.Nullable;
import java.util.Objects;

public final class LimitedFreedomConstraint implements IKConstraint {
    private final Axis2D axis;
    private final float clampedFrom;
    private final float clampedTo;

    public LimitedFreedomConstraint(Axis2D axis, float degreeClampedFrom, float degreeClampedTo) {
        this.axis = axis;
        this.clampedFrom = degreeClampedFrom * BCMath.FRAD;
        this.clampedTo = degreeClampedTo * BCMath.FRAD;
    }

    @Override
    public void applyToSection(IKPartData parentData, IKPartData data, @Nullable IKPartData childData) {
        float unclamped = data.getAngleByAxis(this.axis);
        if (unclamped < this.clampedFrom) {
            data.setAngleByAxis(this.axis, this.clampedFrom);
        } else if (unclamped > this.clampedTo) {
            data.setAngleByAxis(this.axis, this.clampedTo);
        }
    }

    public Axis2D axis() {
        return axis;
    }

    public float clampedFrom() {
        return clampedFrom;
    }

    public float clampedTo() {
        return clampedTo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (LimitedFreedomConstraint) obj;
        return Objects.equals(this.axis, that.axis) &&
                Float.floatToIntBits(this.clampedFrom) == Float.floatToIntBits(that.clampedFrom) &&
                Float.floatToIntBits(this.clampedTo) == Float.floatToIntBits(that.clampedTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(axis, clampedFrom, clampedTo);
    }

    @Override
    public String toString() {
        return "LimitedFreedomConstraint[" +
                "axis=" + axis + ", " +
                "clampedFrom=" + clampedFrom + ", " +
                "clampedTo=" + clampedTo + ']';
    }

}
