package bottomtextdanny.braincell.mod.rendering;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;

@OnlyIn(Dist.CLIENT)
public final class BigItemRenderer extends BlockEntityWithoutLevelRenderer {
    public static final IItemRenderProperties AS_PROPERTY = new IItemRenderProperties() {
        @Override
        public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
            return new BigItemRenderer();
        }
    };

    public BigItemRenderer() {
        super(null, null);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        poseStack.pushPose();
        BakedModel ibakedmodel = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getModelManager().getModel(
                new ModelResourceLocation(stack.getItem().getRegistryName() + "_handheld", "inventory"));
        boolean flag =
                transformType == ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND ||
                transformType == ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND;

        poseStack.translate(0.5F, 0.5F, 0.5F);

        if (transformType == ItemTransforms.TransformType.GUI) {
            ibakedmodel = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getModelManager().getModel(
                    new ModelResourceLocation(stack.getItem().getRegistryName() + "_gui", "inventory")
            );
            renderItemModelIntoGUI(stack, combinedLight, ibakedmodel);
        } else {
            Minecraft.getInstance().getItemRenderer().render(stack, transformType, flag, poseStack, buffer, combinedLight, combinedOverlay, ibakedmodel);
        }

        poseStack.popPose();
    }

    public static void renderItemModelIntoGUI(ItemStack stack, int combinedLight, BakedModel model) {
        RenderSystem.getModelViewStack().pushPose();
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        Minecraft.getInstance().textureManager.getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        PoseStack poseStack = new PoseStack();
        MultiBufferSource.BufferSource irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();

        Lighting.setupForFlatItems();

        Minecraft.getInstance().getItemRenderer().render(stack, ItemTransforms.TransformType.GUI, false, poseStack, irendertypebuffer$impl, combinedLight, OverlayTexture.NO_OVERLAY, model);
        irendertypebuffer$impl.endBatch();
        RenderSystem.enableDepthTest();
        Lighting.setupFor3DItems();


        RenderSystem.getModelViewStack().popPose();
    }
}
