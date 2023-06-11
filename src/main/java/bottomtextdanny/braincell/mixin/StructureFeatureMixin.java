package bottomtextdanny.braincell.mixin;

import java.util.Map;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({StructureFeature.class})
public interface StructureFeatureMixin {
   @Accessor("STEP")
   static Map getSTEP() {
      throw new UnsupportedOperationException();
   }
}
