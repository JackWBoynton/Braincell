package bottomtextdanny.braincell.mod._base.registry.item_extensions;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface ExtraItemModelLoader extends IForgeRegistryEntry {
   @OnlyIn(Dist.CLIENT)
   void bake(ModelRegistryEvent var1);
}
