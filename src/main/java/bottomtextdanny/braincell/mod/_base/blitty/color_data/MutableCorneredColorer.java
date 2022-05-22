package bottomtextdanny.braincell.mod._base.blitty.color_data;

import net.minecraft.util.Mth;

import java.util.Objects;

public final class MutableCorneredColorer implements BlittyColorTransformer {
    private final BlittyColorOperation operation;
    private float topLeftRed;
    private float topLeftGreen;
    private float topLeftBlue;
    private float topLeftAlpha;
    private float topRightRed;
    private float topRightGreen;
    private float topRightBlue;
    private float topRightAlpha;
    private float bottomLeftRed;
    private float bottomLeftGreen;
    private float bottomLeftBlue;
    private float bottomLeftAlpha;
    private float bottomRightRed;
    private float bottomRightGreen;
    private float bottomRightBlue;
    private float bottomRightAlpha;

    public MutableCorneredColorer(
            BlittyColorOperation operation,
            float topLeftRed, float topLeftGreen, float topLeftBlue, float topLeftAlpha,
            float topRightRed, float topRightGreen, float topRightBlue, float topRightAlpha,
            float bottomLeftRed, float bottomLeftGreen, float bottomLeftBlue, float bottomLeftAlpha,
            float bottomRightRed, float bottomRightGreen, float bottomRightBlue, float bottomRightAlpha) {
        this.operation = operation;
        this.topLeftRed = topLeftRed;
        this.topLeftGreen = topLeftGreen;
        this.topLeftBlue = topLeftBlue;
        this.topLeftAlpha = topLeftAlpha;
        this.topRightRed = topRightRed;
        this.topRightGreen = topRightGreen;
        this.topRightBlue = topRightBlue;
        this.topRightAlpha = topRightAlpha;
        this.bottomLeftRed = bottomLeftRed;
        this.bottomLeftGreen = bottomLeftGreen;
        this.bottomLeftBlue = bottomLeftBlue;
        this.bottomLeftAlpha = bottomLeftAlpha;
        this.bottomRightRed = bottomRightRed;
        this.bottomRightGreen = bottomRightGreen;
        this.bottomRightBlue = bottomRightBlue;
        this.bottomRightAlpha = bottomRightAlpha;
    }

    @Override
    public void transform(BlittyColor color) {
        color.r = this.operation.func.apply(color.r, Mth.lerp(color.transitionY,
                Mth.lerp(color.transitionX, this.topLeftRed, this.topRightRed),
                Mth.lerp(color.transitionX, this.bottomLeftRed, this.bottomRightRed)));
        color.g = this.operation.func.apply(color.g, Mth.lerp(color.transitionY,
                Mth.lerp(color.transitionX, this.topLeftGreen, this.topRightGreen),
                Mth.lerp(color.transitionX, this.bottomLeftGreen, this.bottomRightGreen)));
        color.b = this.operation.func.apply(color.b, Mth.lerp(color.transitionY,
                Mth.lerp(color.transitionX, this.topLeftBlue, this.topRightBlue),
                Mth.lerp(color.transitionX, this.bottomLeftBlue, this.bottomRightBlue)));
        color.b = this.operation.func.apply(color.b, Mth.lerp(color.transitionY,
                Mth.lerp(color.transitionX, this.topLeftAlpha, this.topRightAlpha),
                Mth.lerp(color.transitionX, this.bottomLeftAlpha, this.bottomRightAlpha)));
    }

    public void setTopLeft(float red, float green, float blue, float alpha) {
        this.topLeftRed = red;
        this.topLeftGreen = green;
        this.topLeftBlue = blue;
        this.topLeftAlpha = alpha;
    }

    public void setTopRight(float red, float green, float blue, float alpha) {
        this.topRightRed = red;
        this.topRightGreen = green;
        this.topRightBlue = blue;
        this.topRightAlpha = alpha;
    }

    public void setBottomLeft(float red, float green, float blue, float alpha) {
        this.bottomLeftRed = red;
        this.bottomLeftGreen = green;
        this.bottomLeftBlue = blue;
        this.bottomLeftAlpha = alpha;
    }

    public void setBottomRight(float red, float green, float blue, float alpha) {
        this.bottomRightRed = red;
        this.bottomRightGreen = green;
        this.bottomRightBlue = blue;
        this.bottomRightAlpha = alpha;
    }

    public float topLeftRed() {
        return topLeftRed;
    }

    public float topLeftGreen() {
        return topLeftGreen;
    }

    public float topLeftBlue() {
        return topLeftBlue;
    }

    public float topLeftAlpha() {
        return topLeftAlpha;
    }

    public float topRightRed() {
        return topRightRed;
    }

    public float topRightGreen() {
        return topRightGreen;
    }

    public float topRightBlue() {
        return topRightBlue;
    }

    public float topRightAlpha() {
        return topRightAlpha;
    }

    public float bottomLeftRed() {
        return bottomLeftRed;
    }

    public float bottomLeftGreen() {
        return bottomLeftGreen;
    }

    public float bottomLeftBlue() {
        return bottomLeftBlue;
    }

    public float bottomLeftAlpha() {
        return bottomLeftAlpha;
    }

    public float bottomRightRed() {
        return bottomRightRed;
    }

    public float bottomRightGreen() {
        return bottomRightGreen;
    }

    public float bottomRightBlue() {
        return bottomRightBlue;
    }

    public float bottomRightAlpha() {
        return bottomRightAlpha;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (MutableCorneredColorer) obj;
        return this.topLeftRed == that.topLeftRed &&
                this.topLeftGreen == that.topLeftGreen &&
                this.topLeftBlue == that.topLeftBlue &&
                this.topLeftAlpha == that.topLeftAlpha &&
                this.topRightRed == that.topRightRed &&
                this.topRightGreen == that.topRightGreen &&
                this.topRightBlue == that.topRightBlue &&
                this.topRightAlpha == that.topRightAlpha &&
                this.bottomLeftRed == that.bottomLeftRed &&
                this.bottomLeftGreen == that.bottomLeftGreen &&
                this.bottomLeftBlue == that.bottomLeftBlue &&
                this.bottomLeftAlpha == that.bottomLeftAlpha &&
                this.bottomRightRed == that.bottomRightRed &&
                this.bottomRightGreen == that.bottomRightGreen &&
                this.bottomRightBlue == that.bottomRightBlue &&
                this.bottomRightAlpha == that.bottomRightAlpha;
    }

    @Override
    public int hashCode() {
        return Objects.hash(topLeftRed, topLeftGreen, topLeftBlue, topLeftAlpha, topRightRed, topRightGreen, topRightBlue, topRightAlpha, bottomLeftRed, bottomLeftGreen, bottomLeftBlue, bottomLeftAlpha, bottomRightRed, bottomRightGreen, bottomRightBlue, bottomRightAlpha);
    }

    @Override
    public String toString() {
        return "MutableColorData[" +
                "topLeftRed=" + topLeftRed + ", " +
                "topLeftGreen=" + topLeftGreen + ", " +
                "topLeftBlue=" + topLeftBlue + ", " +
                "topLeftAlpha=" + topLeftAlpha + ", " +
                "topRightRed=" + topRightRed + ", " +
                "topRightGreen=" + topRightGreen + ", " +
                "topRightBlue=" + topRightBlue + ", " +
                "topRightAlpha=" + topRightAlpha + ", " +
                "bottomLeftRed=" + bottomLeftRed + ", " +
                "bottomLeftGreen=" + bottomLeftGreen + ", " +
                "bottomLeftBlue=" + bottomLeftBlue + ", " +
                "bottomLeftAlpha=" + bottomLeftAlpha + ", " +
                "bottomRightRed=" + bottomRightRed + ", " +
                "bottomRightGreen=" + bottomRightGreen + ", " +
                "bottomRightBlue=" + bottomRightBlue + ", " +
                "bottomRightAlpha=" + bottomRightAlpha + ']';
    }
}
