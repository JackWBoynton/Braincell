package bottomtextdanny.braincell.mod._base.animation.interpreter;

import bottomtextdanny.braincell.mod._base.animation.ModelAnimator;
import bottomtextdanny.braincell.mod._base.rendering.core_modeling.BCJoint;
import it.unimi.dsi.fastutil.floats.Float2ObjectOpenHashMap;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract sealed class AnimationRunner {
   private AnimationRunner() {
   }

   public void run(List indices, Map timedInstructions, ModelAnimator animator) {
      int iterations = 0;

      for(Iterator var5 = timedInstructions.entrySet().iterator(); var5.hasNext(); ++iterations) {
         Map.Entry entry = (Map.Entry)var5.next();
         Float frame = (Float)entry.getKey();
         List instructionListed = (List)entry.getValue();
         List frameIndices = (List)indices.get(iterations);
         animator.setupKeyframe(frame);
         Iterator var10 = instructionListed.iterator();

         while(var10.hasNext()) {
            AnimationInstruction instruction = (AnimationInstruction)var10.next();
            instruction.actor().act(animator, (BCJoint)frameIndices.get(instruction.index()), instruction.x(), instruction.y(), instruction.z(), instruction.easing());
         }

         animator.apply();
      }

      animator.reset();
   }

   protected abstract Map instructions();

   public static final class BuiltAnimationRunner {
      private final Map instructions;

      public BuiltAnimationRunner(Map instructions) {
         this.instructions = Collections.unmodifiableMap(instructions);
      }

      public Map instructions() {
         return this.instructions;
      }
   }

   public static final class UnbuiltAnimationRunner extends AnimationRunner {
      private final Float2ObjectOpenHashMap instructions = new Float2ObjectOpenHashMap();

      public void put(float frame, List instructions) {
         if (this.instructions.containsKey(frame)) {
            ((List)this.instructions.get(frame)).addAll(instructions);
         } else {
            this.instructions.put(frame, instructions);
         }

      }

      public BuiltAnimationRunner build() {
         return new BuiltAnimationRunner(this.instructions);
      }

      public Map instructions() {
         return this.instructions;
      }
   }
}
