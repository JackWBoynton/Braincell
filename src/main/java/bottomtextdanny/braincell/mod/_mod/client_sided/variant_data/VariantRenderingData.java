package bottomtextdanny.braincell.mod._mod.client_sided.variant_data;

import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface VariantRenderingData<T extends LivingEntity> {

    EntityModel<T> getModel(T entity);

    ResourceLocation getTexture(T entity);
}
