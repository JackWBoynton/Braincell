/*
 * Copyright Saturday October 01 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.particle.client.limb;

import bottomtextdanny.braincell.libraries.model.BCBox;
import bottomtextdanny.braincell.libraries.model.BCTexturedQuad;
import bottomtextdanny.braincell.libraries.model.BCVertex;
import bottomtextdanny.braincell.libraries.model.ImpreciseRot;
import bottomtextdanny.braincell.libraries.particle.ModularParticleData;
import bottomtextdanny.braincell.libraries.particle.client.ModularParticle;
import bottomtextdanny.braincell.libraries.particle.client.tickers.ParticleAction;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class LimbParticle extends ModularParticle {
	protected final Limb limb;

	protected LimbParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Limb limb, ParticleAction start, ParticleAction ticker) {
		super(world, x, y, z, xSpeed, ySpeed, zSpeed, start, ticker);
		this.limb = limb;
	}

	@Override
	public void customInit() {}

	public void tick() {
		super.tick();
	}

	@Override
	public void render(VertexConsumer buffer, Camera camera, float tickOffset) {
		Limb limb = this.limb;
		net.minecraftforge.client.event.ViewportEvent.ComputeCameraAngles cameraSetup = net.minecraftforge.client.ForgeHooksClient.onCameraSetup(Minecraft.getInstance().gameRenderer, camera, tickOffset);
		camera.setAnglesInternal(cameraSetup.getYaw(), cameraSetup.getPitch());

		MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers().bufferSource();
		buffer = source.getBuffer(limb.renderType());
		PoseStack pose = new PoseStack();

		Vec3 cameraPos = camera.getPosition();
		float wX = (float)(Mth.lerp(tickOffset, xo, x) - cameraPos.x());
		float wY = (float)(Mth.lerp(tickOffset, yo, y) - cameraPos.y());
		float wZ = (float)(Mth.lerp(tickOffset, zo, z) - cameraPos.z());
		pose.translate(wX, wY, wZ);
		if (lookToCamera) {
			pose.mulPose(ImpreciseRot.zRotDeg(Mth.lerp(tickOffset, zRotO, zRot)));
			pose.mulPose(ImpreciseRot.xRotDeg(Mth.lerp(tickOffset, xRotO, xRot) + camera.getXRot()));
			pose.mulPose(ImpreciseRot.yRotDeg(Mth.lerp(tickOffset, yRotO, yRot) + camera.getYRot()));
		} else {
			pose.mulPose(ImpreciseRot.zRotDeg(Mth.lerp(tickOffset, zRotO, zRot)));
			pose.mulPose(ImpreciseRot.xRotDeg(Mth.lerp(tickOffset, xRotO, xRot)));
			pose.mulPose(ImpreciseRot.yRotDeg(Mth.lerp(tickOffset, yRotO, yRot)));
		}
		pose.scale(-1.0F, -1.0F, 1.0F);

		int j = getLightColor(tickOffset);
		float rCol = this.rCol;
		float gCol = this.gCol;
		float bCol = this.bCol;
		float alpha = this.alpha;

		Matrix4f matrixPosition = pose.last().pose();
		Matrix3f normal = pose.last().normal();
		Vector3f offset = limb.boxOffset();
		for (BCBox box : limb.boxes()) {
			for (Direction value : Direction.values()) {
				BCTexturedQuad quad = box.getQuad(value);

				if (quad == null) continue;

				Vector3f vector3f = quad.normal.copy();
				vector3f.transform(normal);
				wX = vector3f.x();
				wY = vector3f.y();
				wZ = vector3f.z();

				for (int i = 0; i < 4; ++i) {
					BCVertex vertex = quad.getVertex(i);
					float[] uv = quad.getUV(i);

					float x = vertex.getX() / 16.0F - offset.x();
					float y = vertex.getY() / 16.0F - offset.y();
					float z = vertex.getZ() / 16.0F - offset.z();
					Vector4f vector4f = new Vector4f(x, y, z, 1.0F);
					vector4f.transform(matrixPosition);

					buffer.vertex(vector4f.x(), vector4f.y(), vector4f.z(), rCol, gCol, bCol, alpha, uv[0], uv[1], OverlayTexture.NO_OVERLAY, j, wX, wY, wZ);
				}
			}
		}

		source.endBatch();
	}

	public ParticleRenderType getRenderType() {
		return ParticleRenderType.CUSTOM;
	}

	@Override
	public void move(double x, double y, double z) {
		double d0 = x;
		double d1 = y;
		double d2 = z;
		if (hasPhysics && (x != 0.0D || y != 0.0D || z != 0.0D)) {
			Vec3 vector3d = Entity.collideBoundingBox(null, new Vec3(x, y, z), getBoundingBox(), level, List.of());
			x = vector3d.x;
			y = vector3d.y;
			z = vector3d.z;
		}

		if (x != 0.0D || y != 0.0D || z != 0.0D) {
			setBoundingBox(getBoundingBox().move(x, y, z));
			setLocationFromBoundingbox();
		}

		onGround = d1 != y && d1 < 0.0D;
		if (d0 != x) {
			xd = 0.0D;
		}

		if (d2 != z) {
			zd = 0.0D;
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements ParticleProvider<ModularParticleData> {

		public Factory(SpriteSet spriteSet) {}

		public Particle createParticle(ModularParticleData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			ParticleAction ticker = typeIn.fetch(1);
			ParticleAction start = typeIn.fetch(0);
			return new LimbParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.fetch(2), start == null ? ParticleAction.NO : start, ticker == null ? ParticleAction.NO : ticker);
		}
	}
}
