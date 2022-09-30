package bottomtextdanny.braincell.libraries.speck;

import com.mojang.blaze3d.vertex.PoseStack;
import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.point_lighting.SimplePointLight;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShootLightSpeck extends PointLightSpeck {
    private final int startTicks, staticTicks, endTicks;

    private float alphaScale, prevAlphaScale;

    public ShootLightSpeck(Level world, int startTicks, int staticTicks, int endTicks) {
        super(world);
        this.startTicks = startTicks;
        this.staticTicks = staticTicks;
        this.endTicks = endTicks;
    }

    @Override
    public void tick() {
        super.tick();
        this.prevAlphaScale = this.alphaScale;

        if (getTicksExisted() < this.startTicks) {
            this.alphaScale = (float) getTicksExisted() / (float) this.startTicks;
        } else if (getTicksExisted() < this.staticTicks + this.startTicks) {
            this.alphaScale = 1.0F;
        } else {
            this.alphaScale = 1.0F - (float)(getTicksExisted() - this.startTicks - this.staticTicks) / (float) this.endTicks;
        }
    }

    @Override
    public void render(float easedX, float easedY, float easedZ, PoseStack matrixStack, MultiBufferSource buffer, float partialTick) {
        if (this.light != null) {
            float alphaLrp = Mth.lerp(partialTick, this.prevAlphaScale, this.alphaScale);
            if (alphaLrp > 0.0F) {
                if (Braincell.client().getRenderingHandler().getClipping().isSphereInFrustum((float) getPosX(), (float) getPosY(), (float) getPosZ(), this.light.radius() * alphaLrp)) {
                    Braincell.client().getShaderHandler().getLightingWorkflow().addLight(new SimplePointLight(new Vec3(easedX, easedY, easedZ), this.light.color(), this.light.radius() * alphaLrp, this.light.brightness() * alphaLrp, this.light.lightupFactor() * alphaLrp));
                }
            }
        }
    }
}
