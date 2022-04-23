package net.bottomtextdanny.braincell.mod._base.registry.item_extensions;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;

public interface IBigItemModelLoader extends ExtraItemModelLoader {

    @Override
    default void bake(ModelRegistryEvent event) {
        if (getRegistryName() != null) {
            String path = getRegistryName().getPath();
            String namespace = getRegistryName().getNamespace();
            ForgeModelBakery.addSpecialModel(new ModelResourceLocation(namespace + ":" + path + "_gui", "inventory"));
            ForgeModelBakery.addSpecialModel(new ModelResourceLocation(namespace + ":" + path + "_handheld", "inventory"));
        }
    }
}
