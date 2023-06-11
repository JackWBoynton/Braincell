package bottomtextdanny.braincell.mod._mod.client_sided;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.mod._base.BCStaticData;
import bottomtextdanny.braincell.mod._base.opengl.BCClippingHelper;
import bottomtextdanny.braincell.mod._base.opengl.BCFramebuffer;
import bottomtextdanny.braincell.mod.rendering.HandRendering;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

public final class BackendRenderingHandler {
   private BCFramebuffer worldDepthFramebuffer;
   private Matrix4f projectionMatrix;
   private Matrix4f modelViewMatrix;
   private Matrix4f invertedModelViewMatrix;
   private float terrainFogStart;
   private float terrainFogEnd;
   private float[] terrainFogColor;
   private BCClippingHelper frustum;
   private boolean updatedToCurrentFrame;

   public void postProcessing() {
      Camera info = this.mc().gameRenderer.getMainCamera();
      GlStateManager._glBindFramebuffer(36009, this.worldDepthFramebuffer.getId());
      GlStateManager._enableDepthTest();
      GlStateManager._depthFunc(519);
      HandRendering.render(new PoseStack(), info, BCStaticData.partialTick());
      GlStateManager._enableBlend();
      RenderSystem.defaultBlendFunc();
      GlStateManager._disableDepthTest();
      GlStateManager._glBindFramebuffer(36009, this.mainTarget().frameBufferId);
      GlStateManager._glBindFramebuffer(36008, this.mainTarget().frameBufferId);
      Braincell.client().getShaderHandler().postProcessingFrame();
   }

   public void captureWindowModification() {
      Minecraft mc = Minecraft.getInstance();
      RenderTarget fbo = mc.getMainRenderTarget();
      this.worldDepthFramebuffer = new BCFramebuffer(fbo.width, fbo.height, true);
   }

   public void captureInitialization(float partialTick) {
      if (!this.updatedToCurrentFrame) {
         BCStaticData.setPartialTick(partialTick, new BCClientToken());
         this.updatedToCurrentFrame = true;
      }

   }

   public void setDataOutdated() {
      this.updatedToCurrentFrame = false;
   }

   public void captureRenderInitialInformation(PoseStack matrixStackIn, Matrix4f projectionIn, Camera activeRenderInfo) {
      Vec3 vector3d = activeRenderInfo.getPosition();
      double d0 = vector3d.x();
      double d1 = vector3d.y();
      double d2 = vector3d.z();
      this.projectionMatrix = projectionIn.copy();
      this.modelViewMatrix = matrixStackIn.last().pose().copy();
      Matrix4f mat = RenderSystem.getProjectionMatrix().copy();
      mat.transpose();
      mat.invert();
      mat.multiply(this.modelViewMatrix);
      this.invertedModelViewMatrix = mat;
      this.frustum = new BCClippingHelper(matrixStackIn.last().pose(), projectionIn);
      this.frustum.setCameraPosition(d0, d1, d2);
   }

   public void captureLevelDepth() {
      if (this.worldDepthFramebuffer == null) {
         this.worldDepthFramebuffer = new BCFramebuffer(this.mainTarget().width, this.mainTarget().height, true);
      }

      GlStateManager._glBindFramebuffer(36008, this.mainTarget().frameBufferId);
      GlStateManager._glBindFramebuffer(36009, this.worldDepthFramebuffer.getId());
      GlStateManager._glBlitFrameBuffer(0, 0, this.mainTarget().width, this.mainTarget().height, 0, 0, this.worldDepthFramebuffer.width, this.worldDepthFramebuffer.height, 256, 9728);
      GlStateManager._glBindFramebuffer(36009, this.mainTarget().frameBufferId);
   }

   public void captureTerrainFog() {
      this.terrainFogStart = RenderSystem.getShaderFogStart();
      this.terrainFogEnd = RenderSystem.getShaderFogEnd();
      this.terrainFogColor = RenderSystem.getShaderFogColor();
   }

   public BCClippingHelper getClipping() {
      return this.frustum;
   }

   public Matrix4f getProjectionMatrix() {
      return this.projectionMatrix;
   }

   public Matrix4f getModelViewMatrix() {
      return this.modelViewMatrix;
   }

   public Matrix4f getInvertedModelViewMatrix() {
      return this.invertedModelViewMatrix;
   }

   public BCFramebuffer getWorldDepthFramebuffer() {
      return this.worldDepthFramebuffer;
   }

   public float getTerrainFogStart() {
      return this.terrainFogStart;
   }

   public float getTerrainFogEnd() {
      return this.terrainFogEnd;
   }

   public float[] getTerrainFogColor() {
      return this.terrainFogColor;
   }

   private Minecraft mc() {
      return Minecraft.getInstance();
   }

   private RenderTarget mainTarget() {
      return this.mc().getMainRenderTarget();
   }
}
