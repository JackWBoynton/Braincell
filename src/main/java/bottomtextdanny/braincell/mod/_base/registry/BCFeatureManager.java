package bottomtextdanny.braincell.mod._base.registry;

import bottomtextdanny.braincell.mod._base.registry.managing.Wrap;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;

public abstract class BCFeatureManager extends Wrap {
   public static final UnsupportedOperationException CALL_WHEN_NOT_DEFERRED_EX = new UnsupportedOperationException("Cannot call any dependant object because this wrapper is not deferred.");
   public static final UnsupportedOperationException PRE_SOLVING_CALL_EX = new UnsupportedOperationException("Cannot call any dependant object before mod loading.");

   protected BCFeatureManager(ResourceLocation name, Supplier feature) {
      super(name, feature);
   }

   public void solve() {
      super.solve();
   }

   public Feature getFeature() {
      return (Feature)this.obj;
   }

   protected final void checkSolvingState() {
      if (this.getModSolvingState() == null) {
         throw CALL_WHEN_NOT_DEFERRED_EX;
      } else if (!this.getModSolvingState().isOpen()) {
         throw PRE_SOLVING_CALL_EX;
      }
   }
}
