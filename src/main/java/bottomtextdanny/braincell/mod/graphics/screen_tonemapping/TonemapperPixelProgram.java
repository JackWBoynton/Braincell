package bottomtextdanny.braincell.mod.graphics.screen_tonemapping;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.mod._base.opengl.PixelProgram;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TonemapperPixelProgram extends PixelProgram<TonemapWorkflow> {

    public TonemapperPixelProgram(TonemapWorkflow workflow) {
        super(workflow, new ResourceLocation(Braincell.ID, "tonemapper"));
    }

    @Override
    protected void renderSpace() {
        uTextureBinding("diffuse", mainTarget().getColorTextureId());
        uVector3("exponent", this.workflow.getOutChannelMultiplier());
        uFloat("desaturator", this.workflow.getOutDesaturation());
        renderScreenQuad();
    }

    @Override
    public void flow() {
        super.flow();
        finish();
    }
}
