package bottomtextdanny.braincell.libraries.shader;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.shader.enums.ShaderType;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL20;

import javax.annotation.Nullable;
import java.io.StringWriter;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL43.*;

@OnlyIn(Dist.CLIENT)
public abstract class GLProgram<WF extends ShaderWorkflow> {
    public static final Minecraft MC = Minecraft.getInstance();
    public static final RenderTarget MAIN_TARGET = Minecraft.getInstance().getMainRenderTarget();
    protected final WF workflow;
    protected int programID;
    private final ResourceLocation key;
    protected UniformManager uniformManager;
    private int layoutOffset;

    public GLProgram(WF workflow, ResourceLocation key) {
        super();
        this.key = key;
        creation();
        this.workflow = workflow;
    }

    private void creation() {
        this.programID = glCreateProgram();
        System.out.println("id " + programID + " created for program " + key);
    }

    protected void validateProgram() {
        int isLinked = glGetProgrami(this.programID, GL_LINK_STATUS);

        if (isLinked == GL_FALSE) {
            int length = glGetProgrami(this.programID, GL_INFO_LOG_LENGTH);

            Braincell.client().logger.warn("Braincell '{}' program error: {}", this.key, glGetProgramInfoLog(this.programID));

            glDeleteProgram(this.programID);
            this.workflow.invalidate();
        }
    }

    protected void validateShader(int shaderID) {
        int status = glGetShaderi(shaderID, GL_COMPILE_STATUS);

        if (status == GL_FALSE) {
            String log = glGetShaderInfoLog(shaderID);

            if (log.isEmpty())
                Braincell.client().logger.warn("Braincell '{}' shader error, no log provided", this.key);
            else Braincell.client().logger.warn("Braincell '{}' shader error: {}", this.key, log);

            glDeleteProgram(this.programID);
            this.workflow.invalidate();
        }
    }

    public UniformManager createUniformManager() {
        return new UniformManager(this);
    }

    public abstract void flow();

    public void finish() {
        clearProgram();
    }

    protected abstract void renderSpace();

    public void useProgram() {
        glUseProgram(this.programID);
    }

    public void clearProgram() {
        glUseProgram(0);
        this.layoutOffset = 0;
    }

    protected void attachShader(int shaderID) {
        if (!this.workflow.isInvalid()) glAttachShader(this.programID, shaderID);
    }

    public void linkProgram() {
        glLinkProgram(this.programID);
    }

    public void reset() {
        glUseProgram(0);
        glDeleteProgram(this.programID);
        this.layoutOffset = 0;
    }

    public int getProgramID() {
        return this.programID;
    }

    protected static int createShader(ShaderType type) {
        return glCreateShader(type.getAsInt());
    }

    protected static void deleteShader(int... shaderIDs) {
        for (int shaderID : shaderIDs) {
            glDeleteShader(shaderID);
        }
    }

    protected void compileShader(int shaderID, String source) {
        if (!this.workflow.isInvalid() && source != null) {
            glShaderSource(shaderID, source);
            glCompileShader(shaderID);
        }
    }

    @Nullable
    protected String prepareResource(ResourceLocation fileName, ShaderType shaderType) {
        String source = source(fileName);

        if (source == null) return null;

        String[] supplies = getSourceTransformers(shaderType);

        if (supplies != null) {
            int iterations = supplies.length % 2 == 1 ? supplies.length-1 : supplies.length;

            for (int i = 0; i < iterations; i+=2) {
                String hook = supplies[i];
                String supply = supplies[i + 1];

                source = source.replaceAll(hook, supply);
            }
        }



        return source;
    }

    @Nullable
    protected String[] getSourceTransformers(ShaderType type) {
        return null;
    }

    protected String source(ResourceLocation fileName) {
        try {
            StringWriter strBuf = new StringWriter();
            IOUtils.copy(Minecraft.getInstance().getResourceManager().open(fileName), strBuf, "UTF-8");
            return strBuf.toString();
        } catch(Exception e) {
                e.fillInStackTrace();
        }
        return null;
    }

    protected void bindBuffer(ShaderBuffer buffer) {
        glBindBuffer(GL_SHADER_STORAGE_BUFFER, buffer.getId());
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER, this.layoutOffset, buffer.getId());
        this.layoutOffset++;
    }

    protected void uTextureBinding(String location, int textureID) {
        GlStateManager._activeTexture(GL_TEXTURE0 + this.layoutOffset);
        GlStateManager._enableTexture();
        GlStateManager._bindTexture(textureID);
        glUniform1i(this.uniformManager.retrieveLocation(location), this.layoutOffset);
        GlStateManager._disableTexture();
        GlStateManager._activeTexture(GL_TEXTURE0);
        this.layoutOffset++;
    }

    protected void uBoolean(String location, boolean value) {
        glUniform1i(this.uniformManager.retrieveLocation(location), value?1:0);
    }

    protected void uInteger(String location, int value) {
        glUniform1i(this.uniformManager.retrieveLocation(location), value);
    }

    protected void uFloat(String location, float value) {
        glUniform1f(this.uniformManager.retrieveLocation(location), value);
    }

    protected void uMatrix(String location, Matrix4f value) {
        FloatBuffer buf = FloatBuffer.allocate(16);
        value.store(buf);
        glUniformMatrix4fv(this.uniformManager.retrieveLocation(location), false, buf.array());
    }

    protected void uMatrix(String location, float[] value) {
        glUniformMatrix4fv(this.uniformManager.retrieveLocation(location), false, value);
    }

    protected void uVector4(String location, Vec3 value, float w) {
        glUniform4f(this.uniformManager.retrieveLocation(location), (float)value.x, (float)value.y, (float)value.z, w);
    }

    protected void uVector4(String location, float[] value) {
        glUniform4f(this.uniformManager.retrieveLocation(location), value[0], value[1], value[2], value[3]);
    }

    protected void uVector3(String location, Vec3 value) {
        glUniform3f(this.uniformManager.retrieveLocation(location), (float)value.x, (float)value.y, (float)value.z);
    }

    protected void uVector3(String location, Vector3f value) {
        glUniform3f(this.uniformManager.retrieveLocation(location), value.x(), value.y(), value.z());
    }

    protected void uVector2(String location, float x, float y) {
        glUniform2f(this.uniformManager.retrieveLocation(location), x, y);
    }

    protected void uBoolean(String location, boolean value, int arrayPos) {
        glUniform1i(this.uniformManager.retrieveLocation(location + String.valueOf(new char[]{'[', Character.forDigit(arrayPos, 10), ']'})), value?1:0);
    }

    protected void uInteger(String location, int value, int arrayPos) {
        glUniform1i(this.uniformManager.retrieveLocation(location + String.valueOf(new char[]{'[', Character.forDigit(arrayPos, 10), ']'})), value);
    }

    protected void uFloat(String location, float value, int arrayPos) {
        glUniform1f(this.uniformManager.retrieveLocation(location + String.valueOf(new char[]{'[', Character.forDigit(arrayPos, 10), ']'})), value);
    }

    protected void uMatrix(String location, Matrix4f value, int arrayPos) {
        FloatBuffer buf = FloatBuffer.allocate(16);
        value.store(buf);
        glUniformMatrix4fv(this.uniformManager.retrieveLocation(location + String.valueOf(new char[]{'[', Character.forDigit(arrayPos, 10), ']'})), false, buf.array());
    }

    protected void uMatrix(String location, float[] value, int arrayPos) {
        glUniformMatrix4fv(this.uniformManager.retrieveLocation(location + String.valueOf(new char[]{'[', Character.forDigit(arrayPos, 10), ']'})), false, value);
    }

    protected void uVector4(String location, Vec3 value, float w, int arrayPos) {
        glUniform4f(this.uniformManager.retrieveLocation(location + String.valueOf(new char[]{'[', Character.forDigit(arrayPos, 10), ']'})), (float)value.x, (float)value.y, (float)value.z, w);
    }

    protected void uVector3(String location, Vec3 value, int arrayPos) {
        glUniform3f(this.uniformManager.retrieveLocation(location + String.valueOf(new char[]{'[', Character.forDigit(arrayPos, 10), ']'})), (float)value.x, (float)value.y, (float)value.z);
    }

    protected void uVector3(String location, Vector3f value, int arrayPos) {
        glUniform3f(this.uniformManager.retrieveLocation(location + String.valueOf(new char[]{'[', Character.forDigit(arrayPos, 10), ']'})), value.x(), value.y(), value.z());
    }

    protected void uVector2(String location, float x, float y, int arrayPos) {
        glUniform2f(this.uniformManager.retrieveLocation(location + String.valueOf(new char[]{'[', Character.forDigit(arrayPos, 10), ']'})), x, y);
    }

    protected void uBoolean(int location, boolean value) {
        glUniform1i(location, value?1:0);
    }

    protected void uInteger(int location, int value) {
        glUniform1i(location, value);
    }

    protected void uFloat(int location, float value) {
        glUniform1f(location, value);
    }

    protected void uMatrix(int location, Matrix4f value) {
        FloatBuffer buf = FloatBuffer.allocate(16);
        value.store(buf);
        GL20.glUniformMatrix4fv(location, false, buf.array());
    }

    protected void uMatrix(int location, float[] value) {
        glUniformMatrix4fv(location, false, value);
    }

    protected void uVector4(int location, Vec3 value, float w) {
        glUniform4f(location, (float)value.x, (float)value.y, (float)value.z, w);
    }

    protected void uVector3(int location, Vec3 value) {
        glUniform3f(location, (float)value.x, (float)value.y, (float)value.z);
    }

    protected void uVector3(int location, Vector3f value) {
        glUniform3f(location, value.x(), value.y(), value.z());
    }

    protected void uVector2(int location, float x, float y) {
        glUniform2f(location, x, y);
    }

    public ResourceLocation getKey() {
        return key;
    }

    public static Minecraft mc() {
        return Minecraft.getInstance();
    }

    public static RenderTarget mainTarget() {
        return Minecraft.getInstance().getMainRenderTarget();
    }

    public static void renderScreenQuad() {
        Minecraft mc = Minecraft.getInstance();
        int widthP = mc.getMainRenderTarget().width;
        int heightP = mc.getMainRenderTarget().height;

        GlStateManager._colorMask(true, true, true, false);
        GlStateManager._disableDepthTest();
        GlStateManager._depthMask(false);
        GlStateManager._viewport(0, 0, widthP, heightP);
        GlStateManager._disableBlend();

        Matrix4f matrix4f = Matrix4f.orthographic((float)widthP, (float) -heightP, 1000.0F, 3000.0F);
        RenderSystem.setProjectionMatrix(matrix4f);

        float f = (float)widthP;
        float f1 = (float)heightP;

        Tesselator tesselator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferbuilder.vertex(0.0D, f1, 0.0D).color(255, 255, 0, 255).endVertex();
        bufferbuilder.vertex(f, f1, 0.0D).color(255, 255, 0, 255).endVertex();
        bufferbuilder.vertex(f, 0.0D, 0.0D).color(255, 255, 0, 255).endVertex();
        bufferbuilder.vertex(0.0D, 0.0D, 0.0D).color(255, 0, 255, 255).endVertex();
        BufferUploader.draw(bufferbuilder.end());

        GlStateManager._depthMask(true);
        GlStateManager._colorMask(true, true, true, true);
    }
}
