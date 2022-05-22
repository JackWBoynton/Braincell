package bottomtextdanny.braincell.mod.world.helpers;

import bottomtextdanny.braincell.base.BCMath;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public final class MobVecHelper {

    public static Vec2 lookRadians(double x1, double y1, double z1,
                                  double x2, double y2, double z2) {
        double d0 = x1 - x2;
        double d1 = y1 - y2;
        double d2 = z1 - z2;
        double d3 = Mth.sqrt((float) (d0 * d0 + d2 * d2));
        return new Vec2((float)-(Mth.atan2(d1, d3) * BCMath.RAD2DEG), (float)(Mth.atan2(d1, d0) * BCMath.RAD2DEG - 90.0F));
    }

    public static Vec2 lookRadians(Vec3 vec1, Vec3 vec2) {
        double d0 = vec1.x - vec2.x;
        double d1 = vec1.y - vec2.y;
        double d2 = vec1.z - vec2.z;
        double d3 = Mth.sqrt((float) (d0 * d0 + d2 * d2));
        return new Vec2((float)-(Mth.atan2(d1, d3) * BCMath.RAD2DEG), (float)(Mth.atan2(d1, d0) * BCMath.RAD2DEG - 90.0F));
    }

    private MobVecHelper() {}
}
