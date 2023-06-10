package bottomtextdanny.braincell.mod.graphics.point_lighting;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.base.BCMath;
import bottomtextdanny.braincell.base.pair.Pair;
import bottomtextdanny.braincell.mod._base.opengl.ComputationProgram;
import bottomtextdanny.braincell.mod._base.opengl.UniformManager;
import bottomtextdanny.braincell.mod._base.opengl.enums.ShaderType;
import bottomtextdanny.braincell.mod._mod.client_sided.BraincellClientConfig;
import com.mojang.math.Matrix4f;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL41.*;

@OnlyIn(Dist.CLIENT)
public class PointLightingCompProgram extends ComputationProgram<PointLightingWorkflow> {
    public static final int POSITION_RADIUS_POINTER = 0;
    private final List<? extends IPointLight> lightList;

    public PointLightingCompProgram(PointLightingWorkflow workflow, List<? extends IPointLight> lightList) {
        super(workflow, new ResourceLocation(Braincell.ID, "point_lighting"));
        this.lightList = lightList;
    }

    @Override
    public UniformManager createUniformManager() {
        return new UniformManager(this,
                Pair.of("lights[].position_rad", PointLightingWorkflow.MAX_LIGHTS)
        );
    }

    @Override
    protected String[] getSourceTransformers(ShaderType type) {
        // if (type == ShaderType.COMPUTATION) {
        //     BraincellClientConfig config = Braincell.client().config();
        //     int xTileDiv = BCMath.roundUp(config.xTileDivisions(), 4);
        //     int yTileDiv = BCMath.roundUp(config.yTileDivisions(), 4);
        //     return new String[]{
        //             "&xgrid", String.valueOf(xTileDiv),
        //             "&ygrid", String.valueOf(yTileDiv),
        //             "&x1ingrid", String.valueOf(1.0F / xTileDiv),
        //             "&y1ingrid", String.valueOf(1.0F / yTileDiv),
        //             "&xhalf", String.valueOf((xTileDiv) / 2.0F),
        //             "&yhalf", String.valueOf((yTileDiv) / 2.0F),
        //             "&g_square", String.valueOf(xTileDiv * yTileDiv),
        //             "&max_lights", String.valueOf(BCMath.roundUp(config.maxLights(), 4)),
        //             "&region_lights", String.valueOf(BCMath.roundUp(config.maxLightsPerTile(), 4)),
        //             "&debugt", PointLightingWorkflow.DEBUG_ENABLED ? "imageStore(debug, pixel_coords, vec4(lightOffset *  0.2, 0.0, 0.0, 1));" : "",
        //             "&debugf", PointLightingWorkflow.DEBUG_ENABLED ? "imageStore(debug, pixel_coords, vec4(0.0, 0.0, 0.0, 1));" : ""
        //     };
        // } else 
        if (type == ShaderType.FRAGMENT) { // Change ShaderType to FRAGMENT
            BraincellClientConfig config = Braincell.client().config();
            int xTileDiv = BCMath.roundUp(config.xTileDivisions(), 4);
            int yTileDiv = BCMath.roundUp(config.yTileDivisions(), 4);
            return new String[]{
                    "&xgrid", String.valueOf(xTileDiv),
                    "&ygrid", String.valueOf(yTileDiv),
                    "&x1ingrid", String.valueOf(1.0F / xTileDiv),
                    "&y1ingrid", String.valueOf(1.0F / yTileDiv),
                    "&xhalf", String.valueOf((xTileDiv) / 2.0F),
                    "&yhalf", String.valueOf((yTileDiv) / 2.0F),
                    "&g_square", String.valueOf(xTileDiv * yTileDiv),
                    "&max_lights", String.valueOf(BCMath.roundUp(config.maxLights(), 4)),
                    "&region_lights", String.valueOf(BCMath.roundUp(config.maxLightsPerTile(), 4)),
                    "&debugt", PointLightingWorkflow.DEBUG_ENABLED ? "gl_FragColor = vec4(lightOffset *  0.2, 0.0, 0.0, 1);" : "",
                    "&debugf", PointLightingWorkflow.DEBUG_ENABLED ? "gl_FragColor = vec4(0.0, 0.0, 0.0, 1);" : ""
            };
        }
        return super.getSourceTransformers(type);
    }

    @Override
    protected void renderSpace() {
        int lightAmount = this.workflow.lightsRendered();
        Matrix4f projection = Braincell.client().getRenderingHandler().getProjectionMatrix().copy();

        Matrix4f projMatInv = projection.copy();

        projMatInv.invert();

        Matrix4f view = Braincell.client().getRenderingHandler().getModelViewMatrix();

        bindBuffer(this.workflow.tileInformationBlock);
        this.workflow.tileInformationBlock.resetData();

        uTextureBinding("depth", Braincell.client().getRenderingHandler().getWorldDepthFramebuffer().getDepthID());
        this.workflow.debugBuffer.bind(2);

        uInteger("light_count", lightAmount);
        uMatrix("view", view);
        uMatrix("inv_proj", projMatInv);
        uMatrix("proj", projection);

        for (int i = 0; i < lightAmount; i++) {
            IPointLight light = this.lightList.get(i);
            uVector4(this.uniformManager.retrieveLocations(POSITION_RADIUS_POINTER)[i], light.position().subtract(mc().gameRenderer.getMainCamera().getPosition()), light.radius());
        }

        // dispatchProgram();
        // glBindBuffer(GL_SHADER_STORAGE_BUFFER, 0);
        glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }
}
