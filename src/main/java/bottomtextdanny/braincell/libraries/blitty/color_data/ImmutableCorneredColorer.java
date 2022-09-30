package bottomtextdanny.braincell.libraries.blitty.color_data;

import net.minecraft.util.Mth;

public record ImmutableCorneredColorer(
        BlittyColorOperation operation,
        int topLeftRed, int topLeftGreen, int topLeftBlue, int topLeftAlpha,
        int topRightRed, int topRightGreen, int topRightBlue, int topRightAlpha,
        int bottomLeftRed, int bottomLeftGreen, int bottomLeftBlue, int bottomLeftAlpha,
        int bottomRightRed, int bottomRightGreen, int bottomRightBlue, int bottomRightAlpha) implements BlittyColorTransformer {

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
}
