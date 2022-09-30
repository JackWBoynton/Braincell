package bottomtextdanny.braincell.libraries.model_animation.interpreter;

import bottomtextdanny.braincell.base.Easing;
import bottomtextdanny.braincell.libraries.model_animation.ModelAnimator;
import bottomtextdanny.braincell.libraries.model.BCJoint;

@FunctionalInterface
public interface AnimationInstructionActor {
    AnimationInstructionActor ROTATE = ModelAnimator::rotate;
    AnimationInstructionActor POSITION = ModelAnimator::move;
    AnimationInstructionActor SCALE = ModelAnimator::scale;

    void act(ModelAnimator animator, BCJoint joint, float x, float y, float z, Easing easing);
}
