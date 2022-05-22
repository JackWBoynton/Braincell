package bottomtextdanny.braincell.mod._mod.client_sided;

import bottomtextdanny.braincell.mod._base.opengl.GLProgram;
import bottomtextdanny.braincell.mod._mod.client_sided.events.PostProcessingInitEvent;
import bottomtextdanny.braincell.mod.graphics.screen_tonemapping.TonemapWorkflow;
import bottomtextdanny.braincell.mod.graphics.point_lighting.PointLightingWorkflow;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

@OnlyIn(Dist.CLIENT)
public final class ShaderHandler {
    private final TonemapWorkflow tonemapWorkflow;
    private final PointLightingWorkflow lightingWorkflow;

    public ShaderHandler() {
        tonemapWorkflow = new TonemapWorkflow();
        lightingWorkflow = new PointLightingWorkflow();
        PostProcessingInitEvent event = new PostProcessingInitEvent(this);
        MinecraftForge.EVENT_BUS.post(event);
    }

    public void postProcessingFrame() {
        this.lightingWorkflow._processFrame();

        this.tonemapWorkflow._processFrame();

        GLProgram.renderScreenQuad();
    }

    public void postProcessingTick() {
        this.lightingWorkflow._processTick();
        this.tonemapWorkflow._processTick();
    }

    public PointLightingWorkflow getLightingWorkflow() {
        return this.lightingWorkflow;
    }

    public TonemapWorkflow getTonemapWorkflow() {
        return this.tonemapWorkflow;
    }
}
