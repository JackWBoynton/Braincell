package bottomtextdanny.braincell.mod._base.opengl;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.mod._base.opengl.enums.ShaderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static org.lwjgl.opengl.GL20.glUseProgram;

// @OnlyIn(Dist.CLIENT)
// public abstract class ComputationProgram<WF extends ShaderWorkflow> extends GLProgram<WF> {
//     private int xThreads, yThreads, zThreads;
//     private ResourceLocation shaderSource;

//     public ComputationProgram(int xThreads, int yThreads, int zThreads, WF workflow, ResourceLocation key) {
//         super(workflow, key);
//         try {
//             this.xThreads = xThreads;
//             this.yThreads = yThreads;
//             this.zThreads = zThreads;

//             ResourceLocation computationPath = new ResourceLocation(key.getNamespace(), "shaders/computation/" + key.getPath() + ".comp");

//             String computationSource = prepareResource(computationPath, ShaderType.COMPUTATION);

//             this.shaderSource = computationPath;

//             int computationID = createShader(ShaderType.COMPUTATION);

//             compileShader(computationID, computationSource);
//             validateShader(computationID);
//             attachShader(computationID);
//             linkProgram();
//             validateProgram();
//             deleteShader(computationID);
//             this.uniformManager = createUniformManager();
//         } catch(Exception ex) {
//             Braincell.client().logger
//                     .error("failed to generate compute program object, key:{}", this.getKey());
//             ex.printStackTrace();
//         }
//     }

//     public int getXThreads() {
//         return this.xThreads;
//     }

//     public int getYThreads() {
//         return this.yThreads;
//     }

//     public int getZThreads() {
//         return this.zThreads;
//     }

//     public ResourceLocation getShaderSource() {
//         return this.shaderSource;
//     }

//     @Override
//     public void useProgram() {
//         glUseProgram(this.programID);
//     }

//     protected void dispatchProgram() {
//         glDispatchCompute(this.xThreads, this.yThreads, this.zThreads);
//     }

//     @Override
//     public void flow() {
//         useProgram();
//         renderSpace();
//         clearProgram();
//     }
// }


@OnlyIn(Dist.CLIENT)
public abstract class ComputationProgram<WF extends ShaderWorkflow> extends GLProgram<WF> {
    private ResourceLocation shaderSource;

    public ComputationProgram(WF workflow, ResourceLocation key) {
        super(workflow, key);
        try {
            ResourceLocation fragmentPath = new ResourceLocation(key.getNamespace(), "shaders/fragment/" + key.getPath() + ".frag");

            String fragmentSource = prepareResource(fragmentPath, ShaderType.FRAGMENT);

            this.shaderSource = fragmentPath;

            int fragmentID = createShader(ShaderType.FRAGMENT);

            compileShader(fragmentID, fragmentSource);
            validateShader(fragmentID);
            attachShader(fragmentID);
            linkProgram();
            validateProgram();
            deleteShader(fragmentID);
            this.uniformManager = createUniformManager();
        } catch(Exception ex) {
            Braincell.client().logger
                    .error("failed to generate fragment program object, key:{}", this.getKey());
            ex.printStackTrace();
        }
    }

    public ResourceLocation getShaderSource() {
        return this.shaderSource;
    }

    @Override
    public void useProgram() {
        glUseProgram(this.programID);
    }

    @Override
    public void flow() {
        useProgram();
        renderSpace();
        clearProgram();
    }
}
