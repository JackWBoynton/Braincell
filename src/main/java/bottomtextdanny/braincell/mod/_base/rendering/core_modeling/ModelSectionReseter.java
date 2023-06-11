package bottomtextdanny.braincell.mod._base.rendering.core_modeling;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@FunctionalInterface
public interface ModelSectionReseter {
   void reset(BCModel var1);
}
