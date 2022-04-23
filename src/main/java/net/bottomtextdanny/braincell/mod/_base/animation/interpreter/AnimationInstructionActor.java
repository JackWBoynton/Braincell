package net.bottomtextdanny.braincell.mod._base.animation.interpreter;

import net.bottomtextdanny.braincell.mod._base.animation.EntityModelAnimator;
import net.bottomtextdanny.braincell.mod._base.rendering.core_modeling.BCJoint;

@FunctionalInterface
public interface AnimationInstructionActor {
    AnimationInstructionActor ROTATE = EntityModelAnimator::rotate;
    AnimationInstructionActor POSITION = EntityModelAnimator::move;
    AnimationInstructionActor SCALE = EntityModelAnimator::scale;

    void act(EntityModelAnimator animator, BCJoint joint, float x, float y, float z);
}
