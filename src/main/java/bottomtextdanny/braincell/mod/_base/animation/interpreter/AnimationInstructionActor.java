package bottomtextdanny.braincell.mod._base.animation.interpreter;

import bottomtextdanny.braincell.base.Easing;
import bottomtextdanny.braincell.mod._base.animation.ModelAnimator;
import bottomtextdanny.braincell.mod._base.rendering.core_modeling.BCJoint;

@FunctionalInterface
public interface AnimationInstructionActor {
   AnimationInstructionActor ROTATE = ModelAnimator::rotate;
   AnimationInstructionActor POSITION = ModelAnimator::move;
   AnimationInstructionActor SCALE = ModelAnimator::scale;

   void act(ModelAnimator var1, BCJoint var2, float var3, float var4, float var5, Easing var6);
}
