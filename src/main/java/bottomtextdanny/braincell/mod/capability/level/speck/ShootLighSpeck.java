package bottomtextdanny.braincell.mod.capability.level.speck;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.mod.graphics.point_lighting.SimplePointLight;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShootLighSpeck extends PointLightSpeck {
   private final int startTicks;
   private final int staticTicks;
   private final int endTicks;
   private float alphaScale;
   private float prevAlphaScale;

   public ShootLighSpeck(Level world, int startTicks, int staticTicks, int endTicks) {
      super(world);
      this.startTicks = startTicks;
      this.staticTicks = staticTicks;
      this.endTicks = endTicks;
   }

   public void tick() {
      super.tick();
      this.prevAlphaScale = this.alphaScale;
      if (this.getTicksExisted() < this.startTicks) {
         this.alphaScale = (float)this.getTicksExisted() / (float)this.startTicks;
      } else if (this.getTicksExisted() < this.staticTicks + this.startTicks) {
         this.alphaScale = 1.0F;
      } else {
         this.alphaScale = 1.0F - (float)(this.getTicksExisted() - this.startTicks - this.staticTicks) / (float)this.endTicks;
      }

   }

   public void render(float easedX, float easedY, float easedZ, PoseStack matrixStack, MultiBufferSource buffer, float partialTick) {
      if (this.light != null) {
         float alphaLrp = Mth.lerp(partialTick, this.prevAlphaScale, this.alphaScale);
         if (alphaLrp > 0.0F && Braincell.client().getRenderingHandler().getClipping().isSphereInFrustum((float)this.getPosX(), (float)this.getPosY(), (float)this.getPosZ(), this.light.radius() * alphaLrp)) {
            Braincell.client().getShaderHandler().getLightingWorkflow().addLight(new SimplePointLight(new Vec3((double)easedX, (double)easedY, (double)easedZ), this.light.color(), this.light.radius() * alphaLrp, this.light.brightness() * alphaLrp, this.light.lightupFactor() * alphaLrp));
         }
      }

   }
}
