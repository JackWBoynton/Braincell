package bottomtextdanny.braincell.mod._base.animation.interpreter;

import bottomtextdanny.braincell.base.Easing;
import bottomtextdanny.braincell.mod._base.animation.ModelAnimator;
import bottomtextdanny.braincell.mod._base.rendering.core_modeling.BCJoint;

@FunctionalInterface
public interface AnimationInstructionActor {
    AnimationInstructionActor ROTATE = ModelAnimator::rotate;
    AnimationInstructionActor POSITION = ModelAnimator::move;
    AnimationInstructionActor SCALE = ModelAnimator::scale;

    void act(ModelAnimator animator, BCJoint joint, float x, float y, float z, Easing easing);
}
