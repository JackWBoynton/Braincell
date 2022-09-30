package bottomtextdanny.braincell.libraries.registry.item_extensions;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;

public interface IBigItemModelLoader extends ExtraItemModelLoader {

    @Override
    default void bake(ResourceLocation key, ModelEvent.RegisterAdditional event) {
        String path = key.getPath();
        String namespace = key.getNamespace();
        event.register(new ModelResourceLocation(namespace, path + "_gui", "inventory"));
        event.register(new ModelResourceLocation(namespace, path + "_handheld", "inventory"));
    }
}
