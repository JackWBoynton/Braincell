package bottomtextdanny.braincell.mod.entity.modules.animatable;

import java.util.function.Consumer;

public class StartCalloutAnimation extends SimpleAnimation {
   private final Consumer startCallOut;

   public StartCalloutAnimation(int duration, Consumer startCallOut) {
      super(duration);
      this.startCallOut = startCallOut;
   }

   public void onStart(AnimationHandler handler) {
      this.startCallOut.accept(handler.getEntity());
   }
}
