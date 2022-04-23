package net.bottomtextdanny.braincell.mod.entity.modules.animatable;

import net.bottomtextdanny.braincell.mod._base.entity.modules.animatable.AnimationData;

import java.util.function.Supplier;

public class SimpleAnimation extends AbstractAnimation<AnimationData> {

    public SimpleAnimation(int duration) {
        super(duration);
    }

    @Override
    public Supplier<AnimationData> dataForPlay() {
        return () -> AnimationData.NULL;
    }
}
