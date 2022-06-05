package bottomtextdanny.braincell.mod._base.animation.interpreter;

import bottomtextdanny.braincell.base.Easing;

import javax.annotation.Nullable;

public record AnimationInstruction(AnimationInstructionActor actor,
                                   int index, float x, float y, float z, @Nullable Easing easing) {
}
