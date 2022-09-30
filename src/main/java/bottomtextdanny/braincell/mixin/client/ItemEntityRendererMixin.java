package bottomtextdanny.braincell.mixin.client;

import bottomtextdanny.braincell.Braincell;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntityRenderer.class)
public class ItemEntityRendererMixin {

    @Inject(at = @At(value = "HEAD"), method = "render(Lnet/minecraft/world/entity/item/ItemEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", remap = true)
    private void renderHead(ItemEntity f8, float f9, float f11, PoseStack f13, MultiBufferSource f10, int f12, CallbackInfo ci) {
        Braincell.client().hackyItemEntityTracker = f8;
    }

    @Inject(at = @At(value = "TAIL"), method = "render(Lnet/minecraft/world/entity/item/ItemEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", remap = true)
    private void renderTail(ItemEntity f8, float f9, float f11, PoseStack f13, MultiBufferSource f10, int f12, CallbackInfo ci) {
        Braincell.client().hackyItemEntityTracker = null;
    }
}
