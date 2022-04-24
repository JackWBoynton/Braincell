package net.bottomtextdanny.braincell.mod._base.entity.psyche.pos_finder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiPredicate;

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
