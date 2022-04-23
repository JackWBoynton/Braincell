package net.bottomtextdanny.braincell.mod._base.rendering.ik;

import net.bottomtextdanny.braincell.base.Axis2D;

import javax.annotation.Nullable;

public record LockedAxisConstraint(Axis2D axis) implements IKConstraint {
    public static final LockedAxisConstraint X = new LockedAxisConstraint(Axis2D.X);
    public static final LockedAxisConstraint Y = new LockedAxisConstraint(Axis2D.Y);

    @Override
    public void applyToSection(IKPartData parentData, IKPartData data, @Nullable IKPartData childData) {
        data.setAngleByAxis(this.axis, 0.0F);

    }
}
