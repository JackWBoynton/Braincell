package net.bottomtextdanny.braincell.mod.entity.modules.animatable;

import net.bottomtextdanny.braincell.mod._base.entity.modules.animatable.AnimationHandler;

import java.util.function.Supplier;

public class CuttedAnimation extends AbstractAnimation<CuttedAnimationData> {
    private final int cut;

    public CuttedAnimation(int duration, int cut) {
        super(duration);
        this.cut = cut;
    }

    @Override
    public Supplier<CuttedAnimationData> dataForPlay() {
        return CuttedAnimationData::new;
    }

    @Override
    public int progressTick(int progress, AnimationHandler<?> handler) {
        return getData(handler).pass || progress < this.cut ? progress + 1 : progress;
    }

    public void setPass(AnimationHandler<?> handler, boolean pass) {
        getData(handler).pass = pass;
    }
}
