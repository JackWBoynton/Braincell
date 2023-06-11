package bottomtextdanny.braincell.mod.entity.modules.animatable;

import java.util.function.Supplier;

public class SimpleAnimation extends AbstractAnimation {
   public SimpleAnimation(int duration) {
      super(duration);
   }

   public Supplier dataForPlay() {
      return () -> {
         return AnimationData.NULL;
      };
   }
}
