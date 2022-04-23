package net.bottomtextdanny.braincell.mod._base.entity.psyche.pos_finder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiPredicate;

@FunctionalInterface
public interface MobPosPredicate extends BiPredicate<Mob, BlockPos> {

    @NotNull
    @Override
    default MobPosPredicate and(@NotNull BiPredicate<? super Mob, ? super BlockPos> other) {
        Objects.requireNonNull(other);
        return (Mob t, BlockPos u) -> test(t, u) && other.test(t, u);
    }

    default MobPosPredicate negate() {
        return (Mob t, BlockPos u) -> !test(t, u);
    }

    default MobPosPredicate or(BiPredicate<? super Mob, ? super BlockPos> other) {
        Objects.requireNonNull(other);
        return (Mob t, BlockPos u) -> test(t, u) || other.test(t, u);
    }
}
