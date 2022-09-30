package bottomtextdanny.braincell.libraries.point_lighting;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.base.BCMath;
import bottomtextdanny.braincell.base.pair.Pair;
import bottomtextdanny.braincell.libraries.shader.PixelProgram;
import bottomtextdanny.braincell.libraries.shader.UniformManager;
import bottomtextdanny.braincell.libraries.shader.enums.ShaderType;
import bottomtextdanny.braincell.BraincellClientConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
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
            BraincellClientConfig config = Braincell.client().config();
            int xTileDiv = BCMath.roundUp(config.xTileDivisions(), 4);
            int yTileDiv = BCMath.roundUp(config.yTileDivisions(), 4);
            return new String[]{
                    "&xgrid", String.valueOf(xTileDiv),
                    "&ygrid", String.valueOf(yTileDiv),
                    "&g_square", String.valueOf(xTileDiv * yTileDiv),
                    "&max_lights", String.valueOf(BCMath.roundUp(config.maxLights(), 4)),
                    "&region_lights", String.valueOf(BCMath.roundUp(config.maxLightsPerTile(), 4)),
                    "&debug", PointLightingWorkflow.DEBUG_ENABLED ? "dif += vec4(texture(debug, coords.xy).x, 0, 0, 1);" : ""
            };
        } else {
            return super.getSourceTransformers(type);
        }
    }

    @Override
    protected void renderSpace() {
        PointLightingWorkflow workflow = this.workflow;
        UniformManager uniformManager = this.uniformManager;
        Vec3 position = mc().gameRenderer.getMainCamera().getPosition();
        int lightAmount = workflow.lightsRendered();

        bindBuffer(workflow.tileInformationBlock);
        uTextureBinding("diffuse", mainTarget().getColorTextureId());
        uTextureBinding("depth", Braincell.client().getRenderingHandler().getWorldDepthFramebuffer().getDepthID());
        uTextureBinding("debug", workflow.debugBuffer.getId());
        uMatrix("inv_model_view", Braincell.client().getRenderingHandler().getInvertedModelViewMatrix());
        uFloat("fog_start", Braincell.client().getRenderingHandler().getTerrainFogStart() - 5.0F);
        uFloat("fog_end", Braincell.client().getRenderingHandler().getTerrainFogEnd() - 5.0F);
        uVector4("fog_color", Braincell.client().getRenderingHandler().getTerrainFogColor());
        for (int i = 0; i < lightAmount; i++) {
            IPointLight light = this.lightList.get(i);

            uVector2(uniformManager.retrieveLocations(BRIGHTNESS_LIGHTUP_POINTER)[i], light.brightness(), light.lightupFactor());
            uVector3(uniformManager.retrieveLocations(COLOR_POINTER)[i], light.color());
            uVector4(uniformManager.retrieveLocations(POSITION_RADIUS_POINTER)[i], light.position().subtract(position), light.radius());
        }
        renderScreenQuad();
        glBindBuffer(GL_SHADER_STORAGE_BUFFER, 0);
    }
}
