package bottomtextdanny.braincell.mod.entity.psyche.targeting;

import bottomtextdanny.braincell.base.vector.DistanceCalc3;
import net.minecraft.world.entity.Mob;

public interface RangeTest {
   static RangeTest awayFrom(Mob targeter, double range, DistanceCalc3 calculator) {
      return (x, y, z) -> {
         double flatDist = calculator.distance(targeter.getX(), targeter.getY(), targeter.getZ(), x, y, z);
         return flatDist > range ? -1.0 : flatDist;
      };
   }

   double test(double var1, double var3, double var5);
}
