package bottomtextdanny.braincell.libraries.entity_animation.animations;

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
