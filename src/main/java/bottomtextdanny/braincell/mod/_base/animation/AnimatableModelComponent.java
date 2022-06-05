package bottomtextdanny.braincell.mod._base.animation;

import bottomtextdanny.braincell.base.Easing;

import java.util.Map;

public interface AnimatableModelComponent<T> {

    String name();

    T newAnimationData();

    @SuppressWarnings("unchecked")
    default void _animationTransitionerPrevious(Object previous, Object current, float multiplier, float progression, Map<Easing, Float> easingMap) {
        animationTransitionerPrevious((T)previous, (T)current, multiplier, progression, easingMap);
    }

    @SuppressWarnings("unchecked")
    default void _animationTransitionerCurrent(Object current, float multiplier, float progression, Map<Easing, Float> easingMap) {
        animationTransitionerCurrent((T)current, multiplier, progression, easingMap);
    }

    void animationTransitionerPrevious(T previous, T current, float multiplier, float progression, Map<Easing, Float> easingMap);

    void animationTransitionerCurrent(T current, float multiplier, float progression, Map<Easing, Float> easingMap);
}
