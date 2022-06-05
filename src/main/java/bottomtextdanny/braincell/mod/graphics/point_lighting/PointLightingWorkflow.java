package bottomtextdanny.braincell.mod.graphics.point_lighting;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.mod._base.opengl.ShaderBuffer;
import bottomtextdanny.braincell.mod._base.opengl.ShaderWorkflow;
import bottomtextdanny.braincell.mod._base.opengl.TextureBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL42.*;

@OnlyIn(Dist.CLIENT)
public class PointLightingWorkflow extends ShaderWorkflow {
    public static final int MAX_LIGHTS = 256;
    public static final int MAX_LIGHTS_PER_TILE = 48;
    public static final int GRID_SCALE_X = 24;
    public static final int GRID_SCALE_Y = 16;
    public static final boolean DEBUG_ENABLED = false;
    private final PointLightingPixelProgram lightRenderingProgram;
    private final PointLightingCompProgram frustumCalcProgram;
    public final ShaderBuffer tileInformationBlock;
    public final TextureBuffer debugBuffer;
    private final List<IPointLight> lights = new ArrayList<>();
    private int lastRenderedLights;
    private int lightCounter;

    public PointLightingWorkflow() {
        this.lightRenderingProgram = new PointLightingPixelProgram(this, this.lights);
        this.frustumCalcProgram = new PointLightingCompProgram(this, this.lights);

        this.debugBuffer = new TextureBuffer(GRID_SCALE_X, GRID_SCALE_Y);
        this.tileInformationBlock = new ShaderBuffer(GRID_SCALE_X * GRID_SCALE_Y * MAX_LIGHTS_PER_TILE + GRID_SCALE_X * GRID_SCALE_Y);
    }

    @Override
    protected void execute() {
        this.lightCounter = this.lastRenderedLights;

        this.frustumCalcProgram.flow();

        glMemoryBarrier(GL_BUFFER_UPDATE_BARRIER_BIT | GL_PIXEL_BUFFER_BARRIER_BIT);

        this.lightRenderingProgram.flow();

        clearLights();
    }

    @Override
    protected void tick() {
    }

    public boolean addLight(IPointLight light) {
        if (!invalidated && shouldApply() && light != null && this.lights.size() < MAX_LIGHTS) {
            this.lastRenderedLights++;
            return this.lights.add(light);
        }
        return false;
    }

    private void clearLights() {
        this.lastRenderedLights = 0;
        this.lights.clear();
    }

    public boolean drawingLights() {
        return !this.lights.isEmpty();
    }

    public int lightsRendered() {
        return this.lightCounter;
    }

    @Override
    protected boolean shouldApply() {
        return Braincell.client().config().lights();
    }
}
