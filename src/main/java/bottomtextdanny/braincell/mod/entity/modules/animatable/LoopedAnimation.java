package bottomtextdanny.braincell.mod.entity.modules.animatable;

import java.util.function.Supplier;

public class LoopedAnimation extends AbstractAnimation {
   public LoopedAnimation(int duration) {
      super(duration);
   }

   public Supplier dataForPlay() {
      return LoopedAnimationData::new;
   }

   public int progressTick(int progress, AnimationHandler handler) {
      return (progress + 1) % this.getDuration();
   }

   public boolean goal(int progression, AnimationHandler handler) {
      return ((LoopedAnimationData)this.getData(handler)).stop;
   }

   public void setStop(AnimationHandler handler, boolean pass) {
      ((LoopedAnimationData)this.getData(handler)).stop = pass;
   }
}
