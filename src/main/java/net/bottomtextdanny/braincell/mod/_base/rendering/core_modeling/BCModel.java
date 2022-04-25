package net.bottomtextdanny.braincell.mod._base.rendering.core_modeling;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.bottomtextdanny.braincell.base.BCMath;
import net.bottomtextdanny.braincell.mod._base.BCStaticData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.function.Function;

public interface BCModel {
    float RAD = BCMath.FRAD;

    default float getPartialTick() {
        return BCStaticData.partialTick();
    }

    //BB function
    default void setRotationAngle(BCJoint modelRenderer, float x, float y, float z) {
        modelRenderer.defaultAngleX = x;
        modelRenderer.defaultAngleY = y;
        modelRenderer.defaultAngleZ = z;
    }

    default void setRotationAngleDegrees(BCJoint modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x * RAD;
        modelRenderer.yRot = y * RAD;
        modelRenderer.zRot = z * RAD;
    }

    int getTexWidth();

    int getTexHeight();

    void runDefaultState();

    void addReseter(ModelSectionReseter model);

    static Function<ResourceLocation, VertexConsumer> makeSimple(VertexConsumer builder) {
        return ResourceLocation -> {
            return builder;
        };
    }

    static Function<ResourceLocation, VertexConsumer> makeCutoutGetter(MultiBufferSource source) {
        return resourceLocation -> source.getBuffer(RenderType.entityCutout(resourceLocation));
    }

    static Function<ResourceLocation, VertexConsumer> makeSolidGetter(MultiBufferSource source) {
        return resourceLocation -> source.getBuffer(RenderType.entitySolid(resourceLocation));
    }

    static Function<ResourceLocation, VertexConsumer> makeTranslucentGetter(MultiBufferSource source) {
        return resourceLocation -> source.getBuffer(RenderType.entityTranslucent(resourceLocation));
    }

    static Function<ResourceLocation, VertexConsumer> makeTranslucentCulledGetter(MultiBufferSource source) {
        return resourceLocation -> source.getBuffer(RenderType.entityTranslucentCull(resourceLocation));
    }

    static Function<ResourceLocation, VertexConsumer> makeEyesGetter(MultiBufferSource source) {

        return resourceLocation -> source.getBuffer(RenderType.eyes(resourceLocation));
    }

    private static void tryApplyTexture(@Nullable ResourceLocation texture) {
        if (texture != null) {
            if (RenderSystem.getShaderTexture(0) != Minecraft.getInstance().textureManager.getTexture(texture).getId()) {
                RenderSystem.setShaderTexture(0, texture);
            }
        }
    }
}
