package bottomtextdanny.braincell.mod.graphics.screen_tonemapping;

import bottomtextdanny.braincell.mod._base.opengl.PixelProgram;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TonemapperPixelProgram extends PixelProgram {
   public TonemapperPixelProgram(TonemapWorkflow workflow) {
      super(workflow, new ResourceLocation("braincell", "tonemapper"));
   }

   protected void renderSpace() {
      this.uTextureBinding("diffuse", mainTarget().getColorTextureId());
      this.uVector3("exponent", ((TonemapWorkflow)this.workflow).getOutChannelMultiplier());
      this.uFloat("desaturator", ((TonemapWorkflow)this.workflow).getOutDesaturation());
      renderScreenQuad();
   }

   public void flow() {
      super.flow();
      this.finish();
   }
}
