package bottomtextdanny.braincell.libraries.model.apply;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import bottomtextdanny.braincell.libraries.model.BCJoint;
import bottomtextdanny.braincell.libraries.model.BCModel;
import bottomtextdanny.braincell.libraries.model.ModelSectionReseter;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class BCBlockEntityModel<T extends BlockEntity> extends Model implements BCModel {
    private final List<BCJoint> joints = Lists.newArrayList();
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

    public List<BCJoint> getJoints() {
        return Collections.unmodifiableList(this.joints);
    }

    @Override
    public boolean addJoint(BCJoint joint) {
        return this.joints.add(joint);
    }

    public int getTexWidth() {
        return this.texWidth;
    }

    public int getTexHeight() {
        return this.texHeight;
    }


}
