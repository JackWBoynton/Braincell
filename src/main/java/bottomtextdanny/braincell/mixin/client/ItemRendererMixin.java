package bottomtextdanny.braincell.mixin.client;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.mixin_support.ItemEntityExtensor;
import bottomtextdanny.braincell.mod._base.registry.item_extensions.ExtraModelProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @ModifyVariable(
            at = @At(value = "HEAD"),
            method = "render",
            argsOnly = true,
            ordinal = 0,
            remap = true
    )
    private BakedModel hookCustomModel(BakedModel defaultModel, ItemStack renderedItem,
                                       ItemTransforms.TransformType transformType, boolean leftHand,
                                       PoseStack matrixStack, MultiBufferSource source,
                                       int packedOverlay, int packedLight, BakedModel model) {
        ItemEntity hackyStuff = Braincell.client().hackyItemEntityTracker;

        if (hackyStuff instanceof ItemEntityExtensor extensor && hackyStuff.isAddedToWorld() && hackyStuff.getItem() == renderedItem) {
            int customModelKey = extensor.getShowingModel();
            if (customModelKey != -1 && renderedItem.getItem() instanceof ExtraModelProvider provider) {
                BakedModel customModel = provider.fetchModel((ItemRenderer)(Object)this, customModelKey);

                if (customModel != null) {
                    return customModel;
                }
            }
        }

        return defaultModel;
    }
}
