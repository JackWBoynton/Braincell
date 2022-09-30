/*
 * Copyright Friday August 05 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.test;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.mod.BCStaticData;
import bottomtextdanny.braincell.libraries.model_animation.ik.CCDIKSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Vector3f;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class IKRenderer extends EntityRenderer<IKEntity> {
	private static final ResourceLocation TEXTURES = new ResourceLocation(Braincell.ID, "textures/ik.png");
	public IKModel model;

	public IKRenderer(Object manager) {
		this((EntityRendererProvider.Context) manager);
	}

	public IKRenderer(EntityRendererProvider.Context manager) {
		super(manager);
		model = new IKModel();
	}

	@Override
	public void render(IKEntity entity, float yaw, float pitch, PoseStack stack, MultiBufferSource buffer, int light) {
		super.render(entity, yaw, pitch, stack, buffer, light);

		model = new IKModel();
		stack.pushPose();
		stack.scale(-1, -1, 1);
		stack.translate(0, -1.5, 0);
		model.setupAnim(entity, 0, 0, entity.tickCount + BCStaticData.partialTick(), 0, 0);
			CCDIKSystem<IKEntity> system = model.system;
			float g = 1.0F / 32.0F;
			float posX = system.getGoalX() / 16.0F;
			float posY = system.getGoalY() / 16.0F;
			float posZ = system.getGoalZ() / 16.0F;
			VertexConsumer lineBuffer = buffer.getBuffer(RenderType.LINES);
			LevelRenderer.renderLineBox(stack, lineBuffer,
				posX - g, posY - g, posZ - g,
				posX + g, posY + g, posZ + g,
				0.9F, 0.9F, 0.9F, 1.0F, 0.5F, 0.5F, 0.5F);

			g = 1.0F / 16.0F;
			float s = 0.1F / 16.0F;

			for (int i = 0; i <= 3; i++) {
				Vector3f vec = system.getLocalPos(entity, 0, i);
				posX = vec.x() / 16.0F;
				posY = vec.y() / 16.0F;
				posZ = vec.z() / 16.0F;
				LevelRenderer.renderLineBox(stack, lineBuffer,
					posX - g, posY - g, posZ - g,
					posX + g, posY + g, posZ + g,
					0.9F, 0.9F, 0.9F, 1.0F, 0.5F, 0.5F, 0.5F);
				LevelRenderer.renderLineBox(stack, lineBuffer,
					posX - s, posY - s, posZ - s,
					posX + s, posY + s, posZ + s,
					0.9F, 0.9F, 0.9F, 1.0F, 0.5F, 0.5F, 0.5F);
			}
		model.renderToBuffer(stack, buffer.getBuffer(RenderType.entitySolid(getTextureLocation(entity))), light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		stack.popPose();
	}

	public ResourceLocation getTextureLocation(IKEntity entity) {
		return TEXTURES;
	}
}
