package net.bottomtextdanny.braincell.mod._base.animation.interpreter;

import com.google.common.collect.Maps;
import net.bottomtextdanny.braincell.mod._base.animation.EntityModelAnimator;
import net.bottomtextdanny.braincell.mod.rendering.modeling.BCEntityModel;
import net.bottomtextdanny.braincell.mod._base.rendering.core_modeling.BCJoint;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AnimationInterpreter {
    private final BCEntityModel<?> model;
    private final List<Map<Float, List<AnimationInstruction>>> runners;
    private final List<String> jointIdentifiers;
    private Map<Integer, BCJoint> indexedJoints;

    public AnimationInterpreter(BCEntityModel<?> model,
                                AnimationInterpreterData data) {
        super();
        this.model = model;
        this.runners = data.runners();
        this.jointIdentifiers = data.jointIdentifiers();
    }

    public void run(EntityModelAnimator animator) {

        if (this.indexedJoints == null) {
            indexJoints();
        }

        this.runners.forEach(timedInstructions -> {
            runRunner(timedInstructions, animator);
        });
    }

    private void runRunner(Map<Float, List<AnimationInstruction>> timedInstructions,
                          EntityModelAnimator animator) {
        float addedTime = 0;

        for (Map.Entry<Float, List<AnimationInstruction>> entry : timedInstructions.entrySet()) {
            float frame = entry.getKey() - addedTime;
            addedTime += frame;

            List<AnimationInstruction> instructionListed = entry.getValue();

            if (!animator.setupKeyframe(frame)) continue;

            for (AnimationInstruction instruction : instructionListed) {
                BCJoint joint = indexedJoints.get(instruction.index());

                if (joint != null) {

                    instruction.actor().act(animator, joint,
                            instruction.x(), instruction.y(), instruction.z());
                }
            }

            animator.apply();
        }

        animator.reset();
    }

    private void indexJoints() {
        Map<Integer, BCJoint> joints = Maps.newHashMap();

        model.getJoints().forEach(joint -> {

            for (int i = 0; i < jointIdentifiers.size(); i++) {
                String identifier = jointIdentifiers.get(i);

                if (joint.name().equals(identifier)) {

                    joints.put(i, joint);
                }
            }
        });

        this.indexedJoints = Collections.unmodifiableMap(joints);
    }
}
