package bottomtextdanny.braincell.mod._base.blitty.renderer;

import bottomtextdanny.braincell.mod._base.blitty.Blitty;
import bottomtextdanny.braincell.mod._base.blitty.color_data.BlittyColor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import bottomtextdanny.braincell.mod._base.blitty.BlittyConfig;
import bottomtextdanny.braincell.mod._base.blitty.pos_data.BlittyPos;
import bottomtextdanny.braincell.mod._base.blitty.uv_data.BlittyUV;
import net.minecraft.client.renderer.GameRenderer;

public class BlittyQuadRenderer implements BlittyRenderer{
    public static final BlittyQuadRenderer INSTANCE = new BlittyQuadRenderer();

    @Override
    public void render(Blitty blitty, PoseStack matrixStack, float posX, float posY, float posZ, BlittyConfig... configurations) {
        final Matrix4f matrix = matrixStack.last().pose();
        final BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        final float widthBy2 = blitty.width() / 2.0F;
        final float heightBy2 = blitty.width() / 2.0F;

        final BlittyPos posTopLeft = new BlittyPos(0, 0, -1, -1);
        final BlittyPos posTopRight = new BlittyPos(1, 0, 1, -1);
        final BlittyPos posBottomLeft = new BlittyPos(0, 1, -1, 1);
        final BlittyPos posBottomRight = new BlittyPos(1, 1, 1, 1);

        posX += widthBy2;
        posY += heightBy2;

        final BlittyUV uv = new BlittyUV(blitty.image(), blitty.x(), blitty.y(), blitty.width(), blitty.height());

        if (BlittyRenderer.modsColor(configurations)) {
            final BlittyColor colorTopLeft = new BlittyColor(0, 0);
            final BlittyColor colorTopRight = new BlittyColor(1, 0);
            final BlittyColor colorBottomLeft = new BlittyColor(0, 1);
            final BlittyColor colorBottomRight = new BlittyColor(1, 1);

            RenderSystem.setShader(GameRenderer::getPositionTexColorShader);

            for (BlittyConfig configuration : configurations) {
                configuration.start(blitty);
                configuration.vertex(blitty, colorTopLeft, posTopLeft);
                configuration.vertex(blitty, colorTopRight, posTopRight);
                configuration.vertex(blitty, colorBottomLeft, posBottomLeft);
                configuration.vertex(blitty, colorBottomRight, posBottomRight);
                configuration.finish(blitty, uv);
            }

            final float uvXStart = uv.x / (float)blitty.image().width();
            final float uvYStart = uv.y / (float)blitty.image().height();
            final float uvXEnd = (uv.x + uv.width) / (float)blitty.image().width();
            final float uvYEnd = (uv.y + uv.height) / (float)blitty.image().height();

            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

            BlittyColor currentColor = colorBottomLeft;

            bufferbuilder.vertex(matrix,
                    posX + posBottomLeft.x * widthBy2,
                    posY + posBottomLeft.y * widthBy2,
                    posZ)
                    .uv(uvXStart, uvYEnd)
                    .color(
                            (int) currentColor.r,
                            (int) currentColor.g,
                            (int) currentColor.b,
                            (int) currentColor.a
                    ).endVertex();

            currentColor = colorBottomRight;

            bufferbuilder.vertex(
                    matrix,
                    posX + posBottomRight.x * widthBy2,
                    posY + posBottomRight.y * widthBy2,
                    posZ)
                    .uv(uvXEnd, uvYEnd)
                    .color(
                            (int) currentColor.r,
                            (int) currentColor.g,
                            (int) currentColor.b,
                            (int) currentColor.a
                    ).endVertex();

            currentColor = colorTopRight;

            bufferbuilder.vertex(matrix,
                    posX + posTopRight.x * widthBy2,
                    posY + posTopRight.y * widthBy2,
                    posZ)
                    .uv(uvXEnd, uvYStart)
                    .color(
                            (int) currentColor.r,
                            (int) currentColor.g,
                            (int) currentColor.b,
                            (int) currentColor.a
                    ).endVertex();

            currentColor = colorTopLeft;

            bufferbuilder.vertex(matrix,
                    posX + posTopLeft.x * widthBy2,
                    posY + posTopLeft.y * widthBy2,
                    posZ)
                    .uv(uvXStart, uvYStart)
                    .color(
                            (int) currentColor.r,
                            (int) currentColor.g,
                            (int) currentColor.b,
                            (int) currentColor.a
                    ).endVertex();
            bufferbuilder.end();
        } else {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            
            for (BlittyConfig configuration : configurations) {
                configuration.start(blitty);
                configuration.vertex(blitty, null, posTopLeft);
                configuration.vertex(blitty, null, posTopRight);
                configuration.vertex(blitty, null, posBottomLeft);
                configuration.vertex(blitty, null, posBottomRight);
                configuration.finish(blitty, uv);
            }

            final float uvXStart = uv.x / (float)blitty.image().width();
            final float uvYStart = uv.y / (float)blitty.image().height();
            final float uvXEnd = (uv.x + uv.width) / (float)blitty.image().width();
            final float uvYEnd = (uv.y + uv.height) / (float)blitty.image().height();

            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferbuilder.vertex(matrix,
                            posX + posBottomLeft.x * widthBy2,
                            posY + posBottomLeft.y * widthBy2,
                            posZ)
                    .uv(uvXStart, uvYEnd).endVertex();
            bufferbuilder.vertex(
                            matrix,
                            posX + posBottomRight.x * widthBy2,
                            posY + posBottomRight.y * widthBy2,
                            posZ)
                    .uv(uvXEnd, uvYEnd).endVertex();
            bufferbuilder.vertex(matrix,
                            posX + posTopRight.x * widthBy2,
                            posY + posTopRight.y * widthBy2,
                            posZ)
                    .uv(uvXEnd, uvYStart).endVertex();
            bufferbuilder.vertex(matrix,
                            posX + posTopLeft.x * widthBy2,
                            posY + posTopLeft.y * widthBy2,
                            posZ)
                    .uv(uvXStart, uvYStart).endVertex();
            bufferbuilder.end();
        }

        BufferUploader._endInternal(bufferbuilder);
    }
}
