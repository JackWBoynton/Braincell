package bottomtextdanny.braincell.mod.entity.psyche.actions;

import bottomtextdanny.braincell.mod.entity.modules.animatable.Animation;
import bottomtextdanny.braincell.mod.entity.modules.animatable.AnimationHandler;
import bottomtextdanny.braincell.mod.entity.psyche.Action;
import java.util.function.Supplier;
import net.minecraft.world.entity.PathfinderMob;

public class AnimationAction extends Action {
   protected final Supplier animationProvider;
   protected Animation animation;
   protected final AnimationHandler animationHandler;

   public AnimationAction(PathfinderMob mob, Animation animation, AnimationHandler animationModule) {
      super(mob);
      this.animationProvider = () -> {
         return animation;
      };
      this.animationHandler = animationModule;
   }

   public AnimationAction(PathfinderMob mob, Supplier animation, AnimationHandler animationModule) {
      super(mob);
      this.animationProvider = animation;
      this.animationHandler = animationModule;
   }

   public boolean canStart() {
      return this.active();
   }

   protected void start() {
      super.start();
      this.animationHandler.play(this.animation = (Animation)this.animationProvider.get());
   }

   public boolean shouldKeepGoing() {
      return this.active() && this.animationHandler.isPlaying(this.animation);
   }
}
