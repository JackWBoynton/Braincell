package bottomtextdanny.braincell.libraries.psyche.targeting;

import bottomtextdanny.braincell.libraries.chart.help.Distance3;
import net.minecraft.world.entity.Mob;


public interface RangeTest {

    static RangeTest awayFrom(Mob targeter, double range, Distance3 calculator) {
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
