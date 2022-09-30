package bottomtextdanny.braincell.libraries.psyche.pos_finder;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;

import javax.annotation.Nullable;

@FunctionalInterface
public interface MobPosProcessor<T> {
    MobPosProcessor<?> IDENTITY = (root, mob, random, extra) -> root;

    @Nullable
    BlockPos compute(BlockPos root, Mob mob, RandomSource random, T extra);

    default MobPosProcessor<T> then(MobPosProcessor<? super T> other) {
        return (bp, mob, random, extra) -> {
            BlockPos newPos = this.compute(bp, mob, random, extra);
            return newPos == null ? bp : other.compute(newPos, mob, random, extra);
        };
    }

    default <U extends T> MobPosProcessor<U> generic(Class<U> clazz) {
        return (MobPosProcessor<U>)this;
    }

    default <U extends T> MobPosProcessor<U> cast() {
        return (MobPosProcessor<U>)this;
    }
}
