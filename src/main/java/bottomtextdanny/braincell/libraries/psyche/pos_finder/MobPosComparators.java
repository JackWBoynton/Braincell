package bottomtextdanny.braincell.libraries.psyche.pos_finder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;

import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class MobPosComparators {

    public static BiFunction<Mob, Object, Comparator<BlockPos>> compareWalkValue() {
        return (mob, extra) -> mob instanceof PathfinderMob pf ? Comparator.comparingDouble(pos -> pf.getWalkTargetValue(pos)) :
        Comparator.comparingDouble(pos -> mob.getPathfindingMalus(mob.getFeetBlockState().getBlockPathType(mob.level, pos, mob)));
    }

    private MobPosComparators() {}
}
