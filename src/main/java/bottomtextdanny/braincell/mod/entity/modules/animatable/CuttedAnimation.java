package bottomtextdanny.braincell.mod.entity.modules.animatable;

import java.util.function.Supplier;

public class CuttedAnimation extends AbstractAnimation {
   private final int cut;

   public CuttedAnimation(int duration, int cut) {
      super(duration);
      this.cut = cut;
   }

   public Supplier dataForPlay() {
      return CuttedAnimationData::new;
   }

   public int progressTick(int progress, AnimationHandler handler) {
      return !((CuttedAnimationData)this.getData(handler)).pass && progress >= this.cut ? progress : progress + 1;
   }

   public void setPass(AnimationHandler handler, boolean pass) {
      ((CuttedAnimationData)this.getData(handler)).pass = pass;
   }
}
