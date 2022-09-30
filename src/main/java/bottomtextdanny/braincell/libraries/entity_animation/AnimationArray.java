package bottomtextdanny.braincell.libraries.entity_animation;

import bottomtextdanny.braincell.libraries.entity_animation.animations.Animation;

public record AnimationArray(Animation<?>... animations) implements AnimationGetter {
    private static final BCAnimationToken TOKEN = new BCAnimationToken();

    public AnimationArray(Animation<?>... animations) {
        this.animations = animations;
        for (int i = 0; i < animations.length; i++) {
            animations[i].setIndex(i, TOKEN);
        }
    }

    public static AnimationArray merge(AnimationArray manager, Animation<?>... animations) {
        Animation<?>[] merge = new Animation<?>[manager.animations.length + animations.length];
        System.arraycopy(manager.animations, 0, merge, 0, manager.animations.length);
        System.arraycopy(animations, 0, merge, manager.animations.length, animations.length);
        return new AnimationArray(merge);
    }

    public Animation<?> getAnimation(int index) {
        return this.animations[index];
    }

    public int size() {
        return this.animations.length;
    }
}
