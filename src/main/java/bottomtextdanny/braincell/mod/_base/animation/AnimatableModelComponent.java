package bottomtextdanny.braincell.mod._base.animation;

public interface AnimatableModelComponent<T> {

    String name();

    T newAnimationData();

    @SuppressWarnings("unchecked")
    default void _animationTransitionerPrevious(Object previous, float multiplier, float progression, float invertedProgression) {
        animationTransitionerPrevious((T)previous, multiplier, progression, invertedProgression);
    }

    @SuppressWarnings("unchecked")
    default void _animationTransitionerCurrent(Object current, float multiplier, float progression, float invertedProgression) {
        animationTransitionerCurrent((T)current, multiplier, progression, invertedProgression);
    }

    void animationTransitionerPrevious(T previous, float multiplier, float progression, float invertedProgression);

    void animationTransitionerCurrent(T current, float multiplier, float progression, float invertedProgression);
}
