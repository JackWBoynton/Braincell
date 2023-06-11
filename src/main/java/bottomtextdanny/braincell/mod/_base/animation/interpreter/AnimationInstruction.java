package bottomtextdanny.braincell.mod._base.animation.interpreter;

import bottomtextdanny.braincell.base.Easing;
import javax.annotation.Nullable;

public record AnimationInstruction(AnimationInstructionActor actor, int index, float x, float y, float z, @Nullable Easing easing) {
   public AnimationInstruction(AnimationInstructionActor actor, int index, float x, float y, float z, @Nullable Easing easing) {
      this.actor = actor;
      this.index = index;
      this.x = x;
      this.y = y;
      this.z = z;
      this.easing = easing;
   }

   public AnimationInstructionActor actor() {
      return this.actor;
   }

   public int index() {
      return this.index;
   }

   public float x() {
      return this.x;
   }

   public float y() {
      return this.y;
   }

   public float z() {
      return this.z;
   }

   @Nullable
   public Easing easing() {
      return this.easing;
   }
}
