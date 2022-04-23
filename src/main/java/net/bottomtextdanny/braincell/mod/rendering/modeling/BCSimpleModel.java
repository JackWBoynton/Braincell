package net.bottomtextdanny.braincell.mod.rendering.modeling;

import com.google.common.collect.Lists;
import net.bottomtextdanny.braincell.mod._base.rendering.core_modeling.BCModel;
import net.bottomtextdanny.braincell.mod._base.rendering.core_modeling.ModelSectionReseter;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.Function;

public abstract class BCSimpleModel extends Model implements BCModel {
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

    public int getTexWidth() {
        return this.texWidth;
    }

    public int getTexHeight() {
        return this.texHeight;
    }
}
