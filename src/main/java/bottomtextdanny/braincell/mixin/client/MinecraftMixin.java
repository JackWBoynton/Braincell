package bottomtextdanny.braincell.mixin.client;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.mod.rendering.ResizeEvent;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Minecraft.class})
public abstract class MinecraftMixin {
   @Shadow
   @Final
   private RenderTarget mainRenderTarget;

   @Inject(
      at = {@At("TAIL")},
      method = {"resizeDisplay"},
      remap = true
   )
   public void updatePlayerLook(CallbackInfo ci) {
      MinecraftForge.EVENT_BUS.post(new ResizeEvent(this.mainRenderTarget.width, this.mainRenderTarget.height));
      Braincell.client().getRenderingHandler().captureWindowModification();
   }
}
