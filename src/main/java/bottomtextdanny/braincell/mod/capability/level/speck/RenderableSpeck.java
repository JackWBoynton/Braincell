package bottomtextdanny.braincell.mod.capability.level.speck;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface RenderableSpeck {
   default boolean shouldRender() {
      return ((Speck)this).getAreaUnloadedTicks() == 0;
   }

   void render(float var1, float var2, float var3, PoseStack var4, MultiBufferSource var5, float var6);
}
