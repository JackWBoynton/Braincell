package bottomtextdanny.braincell.libraries._minor.chest.client;

import bottomtextdanny.braincell.libraries.model.apply.BCBlockEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import bottomtextdanny.braincell.libraries.model.BCJoint;
import bottomtextdanny.braincell.libraries._minor.chest.BCChestBlockEntity;

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
