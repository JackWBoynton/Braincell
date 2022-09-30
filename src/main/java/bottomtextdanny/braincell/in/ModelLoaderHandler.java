package bottomtextdanny.braincell.in;

import bottomtextdanny.braincell.base.pair.Pair;
import com.google.common.collect.Lists;
import bottomtextdanny.braincell.libraries.registry.item_extensions.ExtraItemModelLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.List;

public final class ModelLoaderHandler {
    private final List<Pair<ResourceLocation, ExtraItemModelLoader>> extraModelLoaders = Lists.newLinkedList();
    private boolean ran;

    public ModelLoaderHandler() {
        super();
    }

    public void sendListeners() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerExtraModelsHook);
    }

    public boolean addModelLoader(ResourceLocation key, ExtraItemModelLoader itemModelLoader) {
        if (!ran) return this.extraModelLoaders.add(Pair.of(key, itemModelLoader));
        return false;
    }

    private void registerExtraModelsHook(ModelEvent.RegisterAdditional event) {
        if (!ran) {
            this.extraModelLoaders.removeIf(loader -> {
                loader.right().bake(loader.left(), event);
                return true;
            });

            ran = true;
        }
    }
}
