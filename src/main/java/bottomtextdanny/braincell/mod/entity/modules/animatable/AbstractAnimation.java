package bottomtextdanny.braincell.mod.entity.modules.animatable;

public abstract class AbstractAnimation implements Animation {
   public static final int NO_IDENTIFIER = -1;
   private int auxId = -1;
   private int index = 0;
   private final int tickDuration;

   public AbstractAnimation(int duration) {
      this.tickDuration = duration;
   }

   public SimpleAnimation identifier(int identifier) {
      this.auxId = identifier;
      return (SimpleAnimation)this;
   }

   public boolean isFrom(int identifier) {
      return this.auxId == identifier;
   }

   public int progressTick(int progress, AnimationHandler handler) {
      return progress + 1;
   }

   public boolean goal(int progression, AnimationHandler handler) {
      return progression > this.tickDuration;
   }

   public int getIndex() {
      return this.index;
   }

   public void setIndex(int index, BCAnimationToken token) {
      if (token == null) {
         throw new IllegalStateException("Invalid token.");
      } else {
         this.index = index;
      }
   }

   public int getDuration() {
      return this.tickDuration;
   }
}
