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

    void render(float easedX, float easedY, float easedZ, PoseStack matrixStack, MultiBufferSource buffer, float partialTick);
}
