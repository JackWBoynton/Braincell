package net.bottomtextdanny.braincell.mod._base.entity.psyche.pos_finder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;

import javax.annotation.Nullable;
import java.util.random.RandomGenerator;

@FunctionalInterface
public interface MobPosProcessor {
    MobPosProcessor IDENTITY = (root, mob, random) -> root;

    @Nullable
    BlockPos compute(BlockPos root, Mob mob, RandomGenerator random);

    default MobPosProcessor then(MobPosProcessor other) {
        return (bp, mob, random) -> {
            BlockPos newPos = this.compute(bp, mob, random);
            return newPos == null ? bp : other.compute(newPos, mob, random);
        };
    }
}
