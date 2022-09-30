package bottomtextdanny.braincell.libraries.model.apply;

import com.google.common.collect.Lists;
import bottomtextdanny.braincell.libraries.model.BCJoint;
import bottomtextdanny.braincell.libraries.model.BCModel;
import bottomtextdanny.braincell.libraries.model.ModelSectionReseter;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public abstract class BCSimpleModel extends Model implements BCModel {
    private final List<BCJoint> joints = Lists.newArrayList();
    private final List<ModelSectionReseter> reseters = Lists.newArrayList();
    public int texWidth, texHeight;

    public BCSimpleModel(Function<ResourceLocation, RenderType> p_103110_) {
        super(p_103110_);
    }

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
