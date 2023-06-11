package bottomtextdanny.braincell.mod.entity.psyche.actions;

import bottomtextdanny.braincell.mod.entity.psyche.Action;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.world.entity.PathfinderMob;

public class ConstantThoughtAction extends Action {
   @Nullable
   protected final Consumer updateFunction;

   public ConstantThoughtAction(PathfinderMob mob, @Nullable Consumer updateFunction) {
      super(mob);
      this.updateFunction = updateFunction;
   }

   public static ConstantThoughtAction withUpdateCallback(PathfinderMob mob, Consumer doOnUpdate) {
      return new ConstantThoughtAction(mob, doOnUpdate);
   }

   protected void update() {
      if (this.updateFunction != null) {
         this.updateFunction.accept(this.mob);
      }

   }

   public final boolean shouldKeepGoing() {
      return this.active();
   }
}
