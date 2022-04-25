package net.bottomtextdanny.braincell.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.bottomtextdanny.braincell.mod.capability.CapabilityHelper;
import net.bottomtextdanny.braincell.mod.capability.level.BCLevelCapability;
import net.bottomtextdanny.braincell.mod.capability.level.SpeckManagerModule;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Shadow @Nullable private ClientLevel level;
    @Shadow @Final private RenderBuffers renderBuffers;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endLastBatch()V", shift = At.Shift.BEFORE), method = "renderLevel", remap = true)
    public void hookSpeckRendering(PoseStack stack, float partialTick, long unused0, boolean unused1, Camera camera, GameRenderer unused2, LightTexture unused3, Matrix4f unused4, CallbackInfo ci) {
        SpeckManagerModule module = CapabilityHelper.get(this.level, BCLevelCapability.TOKEN).getSpeckManager();
        MultiBufferSource.BufferSource multibuffersource = this.renderBuffers.bufferSource();

        module.render(stack, multibuffersource);
    }
}
