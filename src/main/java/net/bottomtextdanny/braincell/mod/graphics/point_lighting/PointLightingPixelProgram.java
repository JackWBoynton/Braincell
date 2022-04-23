package net.bottomtextdanny.braincell.mod.graphics.point_lighting;

import net.bottomtextdanny.braincell.Braincell;
import net.bottomtextdanny.braincell.mod._base.opengl.PixelProgram;
import net.bottomtextdanny.braincell.mod._base.opengl.UniformManager;
import net.bottomtextdanny.braincell.mod._base.opengl.enums.ShaderType;
import net.bottomtextdanny.braincell.base.pair.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL43.GL_SHADER_STORAGE_BUFFER;

@OnlyIn(Dist.CLIENT)
public class PointLightingPixelProgram extends PixelProgram<PointLightingWorkflow> {
    public static final int COLOR_POINTER = 0;
    public static final int BRIGHTNESS_LIGHTUP_POINTER = 1;
    public static final int POSITION_RADIUS_POINTER = 2;
    private final List<? extends IPointLight> lightList;

    public PointLightingPixelProgram(PointLightingWorkflow workflow, List<? extends IPointLight> lightList) {
        super(workflow, new ResourceLocation(Braincell.ID, "point_lighting"));
        this.lightList = lightList;
    }

    @Override
    public UniformManager createUniformManager() {
        return new UniformManager(this,
                Pair.of("lights[].color", PointLightingWorkflow.MAX_LIGHTS),
                Pair.of("lights[].brightness_lightup", PointLightingWorkflow.MAX_LIGHTS),
                Pair.of("lights[].position_rad", PointLightingWorkflow.MAX_LIGHTS)
        );
    }

    @Override
    protected String[] getSourceTransformers(ShaderType type) {
        if (type == ShaderType.FRAGMENT) {
            return new String[]{
                    "&xgrid", String.valueOf(PointLightingWorkflow.GRID_SCALE_X),
                    "&ygrid", String.valueOf(PointLightingWorkflow.GRID_SCALE_Y),
                    "&g_square", String.valueOf(PointLightingWorkflow.GRID_SCALE_X * PointLightingWorkflow.GRID_SCALE_Y),
                    "&max_lights", String.valueOf(PointLightingWorkflow.MAX_LIGHTS),
                    "&region_lights", String.valueOf(PointLightingWorkflow.MAX_LIGHTS_PER_TILE),
                    "&debug", PointLightingWorkflow.DEBUG_ENABLED ? "dif += vec4(texture(debug, coords.xy).x, 0, 0, 1);" : ""
            };
        } else {
            return super.getSourceTransformers(type);
        }
    }

    @Override
    protected void renderSpace() {
        int lightAmount = this.workflow.lightsRendered();

        bindBuffer(this.workflow.tileInformationBlock);
        uTextureBinding("diffuse", MAIN_TARGET.getColorTextureId());
        uTextureBinding("depth", Braincell.client().getRenderingHandler().getWorldDepthFramebuffer().getDepthID());
        uTextureBinding("debug", this.workflow.debugBuffer.getId());
        uMatrix("inv_model_view", Braincell.client().getRenderingHandler().getInvertedModelViewMatrix());
        uFloat("fog_start", Braincell.client().getRenderingHandler().getTerrainFogStart() - 5.0F);
        uFloat("fog_end", Braincell.client().getRenderingHandler().getTerrainFogEnd() - 5.0F);
        uVector4("fog_color", Braincell.client().getRenderingHandler().getTerrainFogColor());
        for (int i = 0; i < lightAmount; i++) {
            IPointLight light = this.lightList.get(i);
            uVector2(this.uniformManager.retrieveLocations(BRIGHTNESS_LIGHTUP_POINTER)[i], light.brightness(), light.lightupFactor());
            uVector3(this.uniformManager.retrieveLocations(COLOR_POINTER)[i], light.color());
            uVector4(this.uniformManager.retrieveLocations(POSITION_RADIUS_POINTER)[i], light.position().subtract(MC.gameRenderer.getMainCamera().getPosition()), light.radius());
        }
        renderScreenQuad();
        glBindBuffer(GL_SHADER_STORAGE_BUFFER, 0);
    }

    @Override
    public void flow() {
        super.flow();
        finish();
    }
}
