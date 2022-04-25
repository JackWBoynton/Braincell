package net.bottomtextdanny.braincell.mod.entity.psyche.targeting;

import net.bottomtextdanny.braincell.base.vector.DistanceCalc;
import net.minecraft.world.entity.Mob;


public interface RangeTest {

    static RangeTest awayFrom(Mob targeter, double range, DistanceCalc calculator) {
        return (x, y, z) -> {
            double flatDist = calculator.distance(targeter.getX(), targeter.getY(), targeter.getZ(), x, y, z);

            if (flatDist > range) {
                return -1.0D;
            }
            return flatDist;
        };
    }

    double test(double x, double y, double z);
}
