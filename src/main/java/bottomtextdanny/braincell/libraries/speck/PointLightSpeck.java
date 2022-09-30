package bottomtextdanny.braincell.libraries.speck;

import bottomtextdanny.braincell.libraries.point_lighting.IPointLight;
import com.mojang.blaze3d.vertex.PoseStack;
import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.point_lighting.SimplePointLight;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PointLightSpeck extends Speck implements RenderableSpeck {
    protected IPointLight light;

    public PointLightSpeck(Level level) {
        super(level);
    }

    @Override
    public void render(float easedX, float easedY, float easedZ, PoseStack matrixStack, MultiBufferSource buffer, float partialTick) {
        if (this.light != null) {
            if (Braincell.client().getRenderingHandler().getClipping().isSphereInFrustum((float) getPosX(), (float) getPosY(), (float) getPosZ(), this.light.radius())) {
                Braincell.client().getShaderHandler().getLightingWorkflow().addLight(
                        new SimplePointLight(
                                new Vec3(easedX, easedY, easedZ),
                                this.light.color(),
                                this.light.radius(),
                                this.light.brightness(),
                                this.light.lightupFactor()
                        )
                );
            }
        }
    }

    public void setLight(IPointLight light) {
        this.light = light;
    }

    public IPointLight getLight() {
        return this.light;
    }
}
