package bottomtextdanny.braincell.mod.entity.psyche.targeting;

import net.minecraft.world.entity.Mob;

import java.util.Objects;

public interface MobMatchPredicate<T> {
    boolean test(Mob Mob, T subject);

    default MobMatchPredicate<T> and(MobMatchPredicate<? super T> other) {
        Objects.requireNonNull(other);
        return (Mob mob, T subject) -> test(mob, subject) && other.test(mob, subject);
    }

    default MobMatchPredicate<T> negate() {
        return (Mob mob, T subject) -> !test(mob, subject);
    }

    default MobMatchPredicate<T> or(MobMatchPredicate<? super T> other) {
        Objects.requireNonNull(other);
        return (Mob mob, T subject) -> test(mob, subject) || other.test(mob, subject);
    }

    @SuppressWarnings("unchecked")
    default <U extends T> MobMatchPredicate<U> cast() {
        return (MobMatchPredicate<U>)this;
    }

    default <U extends T> MobMatchPredicate<U> cast(Class<U> clazz) {
        return cast();
    }
}
