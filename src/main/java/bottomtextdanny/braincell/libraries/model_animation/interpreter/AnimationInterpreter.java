package bottomtextdanny.braincell.libraries.model_animation.interpreter;

import com.google.common.collect.Maps;
import bottomtextdanny.braincell.base.function.Lazy;
import bottomtextdanny.braincell.libraries.model_animation.ModelAnimator;
import bottomtextdanny.braincell.libraries.model.BCJoint;
import bottomtextdanny.braincell.libraries.model.BCModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class AnimationInterpreter {
    private final BCModel model;
    private final Lazy<AnimationInterpreterData> data;
    private Map<Integer, BCJoint> indexedJoints;

    public AnimationInterpreter(BCModel model,
                                Lazy<AnimationInterpreterData> data) {
        super();
        this.model = model;
        this.data = data;
    }

    public void run(ModelAnimator animator) {

        if (this.indexedJoints == null) {
            indexJoints();
        }

        data.get().runners().forEach(timedInstructions -> {
            runRunner(timedInstructions, animator);
        });
    }

    private void runRunner(Map<Float, List<AnimationInstruction>> timedInstructions,
                          ModelAnimator animator) {
        float addedTime = 0;

        for (Map.Entry<Float, List<AnimationInstruction>> entry : timedInstructions.entrySet()) {
            float frame = entry.getKey() - addedTime;
            addedTime = entry.getKey();

            List<AnimationInstruction> instructionListed = entry.getValue();

            if (!animator.setupKeyframe(frame)) continue;

            for (AnimationInstruction instruction : instructionListed) {
                BCJoint joint = indexedJoints.get(instruction.index());

                if (joint != null) {

                    instruction.actor().act(animator, joint,
                            instruction.x(), instruction.y(), instruction.z(), instruction.easing());
                }
            }

            animator.apply();
        }

        animator.reset();
    }

    private void indexJoints() {
        List<String> jointIdentifiers = this.data.get().jointIdentifiers();
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
