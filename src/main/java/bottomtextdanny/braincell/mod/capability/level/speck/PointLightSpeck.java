package bottomtextdanny.braincell.mod.capability.level.speck;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.mod.graphics.point_lighting.IPointLight;
import bottomtextdanny.braincell.mod.graphics.point_lighting.SimplePointLight;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PointLightSpeck extends Speck implements RenderableSpeck {
   protected IPointLight light;

   public PointLightSpeck(Level level) {
      super(level);
   }

   public void render(float easedX, float easedY, float easedZ, PoseStack matrixStack, MultiBufferSource buffer, float partialTick) {
      if (this.light != null && Braincell.client().getRenderingHandler().getClipping().isSphereInFrustum((float)this.getPosX(), (float)this.getPosY(), (float)this.getPosZ(), this.light.radius())) {
         Braincell.client().getShaderHandler().getLightingWorkflow().addLight(new SimplePointLight(new Vec3((double)easedX, (double)easedY, (double)easedZ), this.light.color(), this.light.radius(), this.light.brightness(), this.light.lightupFactor()));
      }

   }

   public void setLight(IPointLight light) {
      this.light = light;
   }

   public IPointLight getLight() {
      return this.light;
   }
}
