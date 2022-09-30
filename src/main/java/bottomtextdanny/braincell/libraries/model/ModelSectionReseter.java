package bottomtextdanny.braincell.libraries.model;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@FunctionalInterface
public interface ModelSectionReseter {

    void reset(BCModel model);
}
