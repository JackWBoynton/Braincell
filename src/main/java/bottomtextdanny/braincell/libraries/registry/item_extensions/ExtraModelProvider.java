package bottomtextdanny.braincell.libraries.registry.item_extensions;

import bottomtextdanny.braincell.libraries.network.Connection;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.common.extensions.IForgeItem;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public interface ExtraModelProvider extends IForgeItem, ExtraItemModelLoader {
    Map<Item, Int2ObjectArrayMap<ModelResourceLocation>> MODEL_FETCH = new HashMap<>();

    default void fetchFrom(ExtraModelData... models) {
        Connection.doClientSide(() -> {
            Int2ObjectArrayMap<ModelResourceLocation> int2ModelMap = new Int2ObjectArrayMap<>(models.length);

            for (ExtraModelData model : models) {
                int2ModelMap.put(model.index(), model.location());
            }

            MODEL_FETCH.put((Item) this, int2ModelMap);
        });
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    default ResourceLocation fetchModelDir(int key) {
        if (MODEL_FETCH.get((Item) this).containsKey(key)) {
            return MODEL_FETCH.get((Item) this).get(key);
        }
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    default BakedModel fetchModel(ItemRenderer itemRenderer, int key) {
        if (MODEL_FETCH.get((Item) this).containsKey(key)) {
            return itemRenderer.getItemModelShaper().getModelManager().getModel(MODEL_FETCH.get((Item) this).get(key));
        }
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    default void bake(ResourceLocation key, ModelEvent.RegisterAdditional event) {
        Int2ObjectArrayMap<ModelResourceLocation> keyMap = MODEL_FETCH.get(this);

        int size = keyMap.size();

        for (int i = 0; i < size; i++) {
            event.register(keyMap.get(i));
        }
    }
}
