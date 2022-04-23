package net.bottomtextdanny.braincell.mod._base.opengl;

import com.mojang.math.Matrix4f;
import net.bottomtextdanny.braincell.Braincell;
import net.bottomtextdanny.braincell.mod._base.opengl.enums.ShaderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumMap;

@OnlyIn(Dist.CLIENT)
public abstract class PixelProgram<WF extends ShaderWorkflow> extends GLProgram<WF> {
    protected static final String PROJ_MATRIX_NAME = "proj_mat";
    protected static final String MODEL_VIEW_MATRIX_NAME = "mv_mat";
    protected static final String SCREN_SIZE_NAME = "screen";
    public final EnumMap<ShaderType, ResourceLocation> shaderSourceMap = new EnumMap<>(ShaderType.class);

    public PixelProgram(WF workflow, ResourceLocation key) {
        super(workflow, key);
        try {
            this.uniformManager = createUniformManager();
            ResourceLocation vertexPath = new ResourceLocation(key.getNamespace(), "shaders/vertex/" + key.getPath() + ".vert");
            ResourceLocation fragmentPath = new ResourceLocation(key.getNamespace(), "shaders/fragment/" + key.getPath() + ".frag");
            ResourceLocation geometryPath = new ResourceLocation(key.getNamespace(), "shaders/geometry/" + key.getPath() + ".geom");

            String vertexSource = prepareResource(vertexPath, ShaderType.VERTEX);
            String fragmentSource = prepareResource(fragmentPath, ShaderType.FRAGMENT);
            String geometrySource = prepareResource(geometryPath, ShaderType.GEOMETRY);

            this.shaderSourceMap.put(ShaderType.VERTEX, vertexPath);
            this.shaderSourceMap.put(ShaderType.FRAGMENT, fragmentPath);

            int vertexID = createShader(ShaderType.VERTEX);
            int fragmentID = createShader(ShaderType.FRAGMENT);
            int geometryID = -1;

            compileShader(vertexID, vertexSource);
            compileShader(fragmentID, fragmentSource);

            boolean geomFlag = geometrySource != null;

            if (geomFlag) {
                this.shaderSourceMap.put(ShaderType.GEOMETRY, geometryPath);

                geometryID = createShader(ShaderType.GEOMETRY);

                compileShader(geometryID, geometrySource);
                attachShader(geometryID);
            }

            attachShader(vertexID);
            attachShader(fragmentID);
            linkProgram();

            if (geomFlag) {
                deleteShader(geometryID);
            }

            deleteShader(vertexID, fragmentID);
        } catch (Exception exception) {
            workflow.invalidate();
            Braincell.client().logger.error("Unexpected error while building {} shader. Skipping...", key);
            exception.printStackTrace();
        }
    }

    @Override
    public void flow() {
        useProgram();
        uMatrix(PROJ_MATRIX_NAME, Matrix4f.orthographic(MC.getWindow().getWidth(), (float) -MC.getWindow().getHeight(), 1000.0F, 3000.0F));
        uVector2(SCREN_SIZE_NAME, MC.getWindow().getWidth(), MC.getWindow().getHeight());
        renderSpace();
    }
}
