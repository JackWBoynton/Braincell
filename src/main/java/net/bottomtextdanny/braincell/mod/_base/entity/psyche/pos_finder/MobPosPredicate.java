package net.bottomtextdanny.braincell.mod._base.entity.psyche.pos_finder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;

import java.util.Objects;

@FunctionalInterface
public interface MobPosPredicate<T> {

    boolean test(Mob mob, BlockPos blockPos, T extra);

    default MobPosPredicate<T> and(MobPosPredicate<? super T> other) {
        Objects.requireNonNull(other);
        return (Mob mob, BlockPos blockPos, T extra) -> test(mob, blockPos, extra) && other.test(mob, blockPos, extra);
    }

    default MobPosPredicate<T> negate() {
        return (Mob mob, BlockPos blockPos, T extra) -> !test(mob, blockPos, extra);
    }

    default MobPosPredicate<T> or(MobPosPredicate<? super T> other) {
        Objects.requireNonNull(other);
        return (Mob mob, BlockPos blockPos, T extra) -> test(mob, blockPos, extra) || other.test(mob, blockPos, extra);
    }
}
