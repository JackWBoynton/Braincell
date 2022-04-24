package net.bottomtextdanny.braincell.mod.entity.psyche.pos_finder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;

import java.util.Comparator;
import java.util.function.Function;

public final class MobPosComparators {

    public static Function<Object, Comparator<BlockPos>> compareWalkValue(Mob mob) {
        return (extra) -> mob instanceof PathfinderMob pf ? Comparator.comparingDouble(pos -> pf.getWalkTargetValue(pos)) :
        Comparator.comparingDouble(pos -> mob.getPathfindingMalus(mob.getFeetBlockState().getBlockPathType(mob.level, pos)));
    }

    private MobPosComparators() {}
}
