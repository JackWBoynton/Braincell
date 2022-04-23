package net.bottomtextdanny.braincell.mod._base.opengl;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class ShaderWorkflow {
    public static final Minecraft MC = Minecraft.getInstance();
    protected boolean invalidated;

    protected abstract void execute();

    protected abstract void tick();

    public void _processFrame() {
        if (!this.invalidated && shouldApply()) {
            execute();
        }
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
