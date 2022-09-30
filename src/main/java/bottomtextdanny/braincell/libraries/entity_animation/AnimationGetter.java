package bottomtextdanny.braincell.libraries.entity_animation;

import bottomtextdanny.braincell.libraries.entity_animation.animations.Animation;

public interface AnimationGetter {

    Animation<?> getAnimation(int index);

    int size();
}
