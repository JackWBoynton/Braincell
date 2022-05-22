package bottomtextdanny.braincell.mod.rendering.modeling.chest;

import bottomtextdanny.braincell.mod.rendering.modeling.BCBlockEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import bottomtextdanny.braincell.mod._base.rendering.core_modeling.BCJoint;
import bottomtextdanny.braincell.mod.world.builtin_block_entities.BCChestBlockEntity;

public abstract class BCChestModel extends BCBlockEntityModel<BCChestBlockEntity> {
    protected BCJoint chest;
    protected BCJoint lid;

    public void setRotationWLid(BCChestBlockEntity chest, float partialTicks, float ligAngle) {
        float lidAngle = 1 - ligAngle;
        this.lid.xRot = (-90.0F + lidAngle * lidAngle * lidAngle * 90.0F) * RAD;
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.chest.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
