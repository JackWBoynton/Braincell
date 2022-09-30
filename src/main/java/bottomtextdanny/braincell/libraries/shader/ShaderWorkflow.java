package bottomtextdanny.braincell.libraries.shader;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class ShaderWorkflow {
    public static final Minecraft MC = Minecraft.getInstance();
    protected boolean invalidated;

    protected abstract void execute();

    protected abstract void tick();

    public boolean _processFrame() {
        boolean apply = shouldApply();
        if (!this.invalidated && apply) {
            execute();
        }

        return apply;
    }

    public void _processTick() {
        if (!this.invalidated && shouldApply()) {
            tick();
        }
    }

    public void finish() {}

    protected abstract boolean shouldApply();

    public void invalidate() {
        this.invalidated = false;
    }

    public boolean isInvalid() {
        return this.invalidated;
    }
}
