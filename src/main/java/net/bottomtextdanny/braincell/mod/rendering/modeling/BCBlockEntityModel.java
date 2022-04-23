package net.bottomtextdanny.braincell.mod.rendering.modeling;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.bottomtextdanny.braincell.mod._base.rendering.core_modeling.BCModel;
import net.bottomtextdanny.braincell.mod._base.rendering.core_modeling.ModelSectionReseter;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public abstract class BCBlockEntityModel<T extends BlockEntity> extends Model implements BCModel {
    private final List<ModelSectionReseter> reseters = Lists.newArrayList();
    public int texWidth, texHeight;

    public BCBlockEntityModel() {
        super(RenderType::entityCutoutNoCull);
    }

    @Override
    abstract public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha);

    @Override
    public void runDefaultState() {
        if (!this.reseters.isEmpty()) this.reseters.forEach(r -> r.reset(this));
    }

    @Override
    public void addReseter(ModelSectionReseter model) {
        this.reseters.add(model);
    }

    public int getTexWidth() {
        return this.texWidth;
    }

    public int getTexHeight() {
        return this.texHeight;
    }


}
