package bottomtextdanny.braincell.mod._base.animation.interpreter;

import it.unimi.dsi.fastutil.floats.Float2ObjectOpenHashMap;
import bottomtextdanny.braincell.mod._base.animation.ModelAnimator;
import bottomtextdanny.braincell.mod._base.rendering.core_modeling.BCJoint;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public sealed abstract class AnimationRunner {

    private AnimationRunner() {
        super();
    }

    public void run(List<List<BCJoint>> indices, Map<Float, List<AnimationInstruction>> timedInstructions, ModelAnimator animator) {
        int iterations = 0;

        for (Map.Entry<Float, List<AnimationInstruction>> entry : timedInstructions.entrySet()) {
            Float frame = entry.getKey();
            List<AnimationInstruction> instructionListed = entry.getValue();
            List<BCJoint> frameIndices = indices.get(iterations);

            animator.setupKeyframe(frame);

            for (AnimationInstruction instruction : instructionListed) {
                instruction.actor().act(animator, frameIndices.get(instruction.index()),
                        instruction.x(), instruction.y(), instruction.z());
            }

            animator.apply();
            iterations++;
        }
        animator.reset();
    }

    protected abstract Map<Float, List<AnimationInstruction>> instructions();

    public static final class UnbuiltAnimationRunner extends AnimationRunner {
        private final Float2ObjectOpenHashMap<List<AnimationInstruction>> instructions = new Float2ObjectOpenHashMap<>();

        public UnbuiltAnimationRunner() {
            super();
        }

        public void put(float frame, List<AnimationInstruction> instructions) {
            if (this.instructions.containsKey(frame)) {
                this.instructions.get(frame).addAll(instructions);
            } else {
                this.instructions.put(frame, instructions);
            }
        }

        public BuiltAnimationRunner build() {
            return new BuiltAnimationRunner(instructions);
        }

        public Map<Float, List<AnimationInstruction>> instructions() {
            return instructions;
        }
    }

    public static final class BuiltAnimationRunner {
        private final Map<Float, List<AnimationInstruction>> instructions;

        public BuiltAnimationRunner(Map<Float, List<AnimationInstruction>> instructions) {
            this.instructions = Collections.unmodifiableMap(instructions);
        }

        public Map<Float, List<AnimationInstruction>> instructions() {
            return instructions;
        }
    }
}
