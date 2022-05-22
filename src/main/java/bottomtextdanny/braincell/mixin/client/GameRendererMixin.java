package bottomtextdanny.braincell.mixin.client;

import bottomtextdanny.braincell.Braincell;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(at = @At(value = "HEAD"), method = "render", remap = true)
    public void updateCameraAndRender(float partialTick, long nanoTime, boolean renderWorldIn, CallbackInfo ci) {
        Braincell.client().getRenderingHandler().setDataOutdated();
        Braincell.client().getRenderingHandler().captureInitialization(partialTick);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V", shift = At.Shift.BEFORE), method = "render", remap = true)
    public void postProcessing(float partialTicks, long nanoTime, boolean renderWorldIn, CallbackInfo ci) {
        Braincell.client().getRenderingHandler().postProcessing();
    }
}
