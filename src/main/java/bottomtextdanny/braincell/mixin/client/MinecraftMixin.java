package bottomtextdanny.braincell.mixin.client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.mod.rendering.ResizeEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow @Final private RenderTarget mainRenderTarget;

    @Inject(at = @At(value = "TAIL"), method = "resizeDisplay", remap = true)
    public void updatePlayerLook(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new ResizeEvent(mainRenderTarget.width, mainRenderTarget.height));
        Braincell.client().getRenderingHandler().captureWindowModification();
    }

}
