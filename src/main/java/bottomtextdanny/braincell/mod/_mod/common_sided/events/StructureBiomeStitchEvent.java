package bottomtextdanny.braincell.mod._mod.common_sided.events;

import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraftforge.eventbus.api.Event;

public final class StructureBiomeStitchEvent extends Event {
   private final BiConsumer registerFunction;

   public StructureBiomeStitchEvent(BiConsumer registerFunction) {
      this.registerFunction = registerFunction;
   }

   public void register(ConfiguredStructureFeature feature, ResourceKey biomeKey) {
      this.registerFunction.accept(feature, biomeKey);
   }
}
