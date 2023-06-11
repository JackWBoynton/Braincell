package bottomtextdanny.braincell.mod._base.registry.item_extensions;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;

public interface IBigItemModelLoader extends ExtraItemModelLoader {
   default void bake(ModelRegistryEvent event) {
      if (this.getRegistryName() != null) {
         String path = this.getRegistryName().getPath()
;
         String namespace = this.getRegistryName().getNamespace();
         ForgeModelBakery.addSpecialModel(new ModelResourceLocation(namespace + ":" + path + "_gui", "inventory"));
         ForgeModelBakery.addSpecialModel(new ModelResourceLocation(namespace + ":" + path + "_handheld", "inventory"));
      }

   }
}
