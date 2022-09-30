package bottomtextdanny.braincell.libraries.registry.item_extensions;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;

public interface ExtraItemModelLoader extends ItemLike {

    @OnlyIn(Dist.CLIENT)
    void bake(ResourceLocation key, ModelEvent.RegisterAdditional event);
}
