package net.bottomtextdanny.braincell.mod._base.entity.modules.animatable;

import net.minecraft.world.entity.Entity;

public class AnimatableModule extends BaseAnimatableModule<AnimatableProvider> {

    public AnimatableModule(Entity entity, AnimationGetter manager) {
        super(entity, manager);
    }
}
