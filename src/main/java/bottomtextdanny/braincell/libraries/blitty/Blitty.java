package bottomtextdanny.braincell.libraries.blitty;

import bottomtextdanny.braincell.libraries.blitty.renderer.BlittyRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import bottomtextdanny.braincell.base.screen.ImageBounds;
import net.minecraft.client.renderer.GameRenderer;

public record Blitty(ImageBounds image, int x, int y, int width, int height) {

    public void render(PoseStack matrixStack, float posX, float posY, float posZ) {
        Matrix4f matrix = matrixStack.last().pose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        ImageBounds image = this.image;
        int width = this.width;
        int height = this.height;
        float x0 = this.x / (float) image.width();
        float y0 = this.y / (float) image.height();
        float x1 = (this.x + width) / (float) image.width();
        float y1 = (this.y + height) / (float) image.height();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix, posX, posY + height, posZ).uv(x0, y1).endVertex();
        bufferbuilder.vertex(matrix, posX + width, posY + height, posZ).uv(x1, y1).endVertex();
        bufferbuilder.vertex(matrix, posX + width, posY, posZ).uv(x1, y0).endVertex();
        bufferbuilder.vertex(matrix, posX, posY, posZ).uv(x0, y0).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }

    public void render(PoseStack matrixStack, float posX, float posY, float posZ, BlittyRenderer renderer, BlittyConfig... configurations) {
        renderer.render(this, matrixStack, posX, posY, posZ, configurations);
    }
}
