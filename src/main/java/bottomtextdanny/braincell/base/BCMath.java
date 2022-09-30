package bottomtextdanny.braincell.base;

import com.mojang.math.Vector3f;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class BCMath {
    public static final double SQRT_2 = 1.4142135623730951;
    public static final double RAD = Math.PI / 180.0;
    public static final float FRAD = (float)RAD;
    public static final float FPI = (float)Math.PI;
    public static final double PI_BY_TWO = Math.PI * 2.0;
    public static final float FPI_BY_TWO = (float)PI_BY_TWO;
    public static final double PI_HALF = Math.PI / 2.0;
    public static final float FPI_HALF = (float)PI_HALF;
    public static final double RAD2DEG = 57.29577951308232;
    public static final float FRAD2DEG = (float)RAD2DEG;

    public static void rotateX(float rad, Vector3f vec) {
        float sin = BCMath.sin(rad);
        float cos = BCMath.cos(rad);
        float y = vec.y();
        vec.setY(y * cos - vec.z() * sin);
        vec.setZ(y * sin + vec.z() * cos);
    }

    public static Vector3f rotatedX(float rad, Vector3f vec) {
        float sin = BCMath.sin(rad);
        float cos = BCMath.cos(rad);
        return new Vector3f(vec.x(), vec.y() * cos - vec.z() * sin,
            vec.y() * sin + vec.z() * cos);
    }

    public static Vec3 rotateX(float rad, Vec3 vec) {
        float sin = BCMath.sin(rad);
        float cos = BCMath.cos(rad);
        return new Vec3(vec.x, vec.y * cos - vec.z * sin,
            vec.y * sin + vec.z * cos);
    }

    public static void rotateY(float rad, Vector3f vec) {
        float sin = BCMath.sin(rad);
        float cos = BCMath.cos(rad);
        float x = vec.x();
        vec.setX(x * cos + vec.z() * sin);
        vec.setZ(-x * sin + vec.z() * cos);
    }

    public static Vector3f rotatedY(float rad, Vector3f vec) {
        float sin = BCMath.sin(rad);
        float cos = BCMath.cos(rad);
        return new Vector3f(vec.x() * cos + vec.z() * sin,
            vec.y(), -vec.x() * sin + vec.z() * cos);
    }

    public static Vec3 rotateY(float rad, Vec3 vec) {
        float sin = BCMath.sin(rad);
        float cos = BCMath.cos(rad);
        return new Vec3(vec.x * cos + vec.z * sin,
            vec.y, -vec.x * sin + vec.z * cos);
    }

    public static void rotateZ(float rad, Vector3f vec) {
        float sin = BCMath.sin(rad);
        float cos = BCMath.cos(rad);
        float x = vec.x();
        vec.setX(x * cos - vec.y() * sin);
        vec.setY(x * sin + vec.y() * cos);
    }

    public static Vector3f rotatedZ(float rad, Vector3f vec) {
        float sin = BCMath.sin(rad);
        float cos = BCMath.cos(rad);
        return new Vector3f(vec.x() * cos - vec.y() * sin,
            vec.x() * sin + vec.y() * cos, vec.z());
    }

    public static Vec3 rotateZ(float rad, Vec3 vec) {
        float sin = BCMath.sin(rad);
        float cos = BCMath.cos(rad);
        return new Vec3(vec.x * cos - vec.y * sin,
            vec.x * sin + vec.y * cos, vec.z);
    }

    public static int roundUp(int val, int alignment) {
        int mod = val % alignment;
        if (mod == 0) return val;
        return val + alignment - mod;
    }

    public static double dot3(final double x, final double y, final double z) {
        return x * x + y * y + z * z;
    }

    public static float dot3(final float x, final float y, final float z) {
        return x * x + y * y + z * z;
    }

    public static double dot2(final double x, final double y) {
        return x * x + y * y;
    }

    public static float dot2(final float x, final float y) {
        return x * x + y * y;
    }

    public static double fastpow(final double val, final double expo) {
        long tmp = Double.doubleToLongBits(val);
        long tmp2 = (long)(expo * (tmp - 4606921280493453312L)) + 4606921280493453312L;
        return Double.longBitsToDouble(tmp2);
    }

    public static float loopLerp(float pct, float loopEnd, float start, float end) {
        float difference;

        if (start > end) {
            difference = loopEnd - start;
            difference += end;
        } else {
            difference = end - start;
        }

        return loop(start, loopEnd, pct * difference);
    }

    public static float loop(float holder, float end, float offset) {
        return (holder + offset) % end;
    }

    public static float zp2Np(float un) {
        return (un - 0.5F) * 2;
    }

    public static float np2Zp(float un) {
        return un / 2 + 0.5F;
    }

    public static float sin(float rad) {
        return Mth.sin(rad);
    }

    public static float cos(float rad) {
        return Mth.cos(rad);
    }

    public static float sin(double rad) {
        return Mth.sin((float)rad);
    }

    public static float cos(double rad) {
        return Mth.cos((float)rad);
    }

    public static float normalizeRad(float radian) {
        radian = radian % FPI_BY_TWO;
        if (radian > FPI) {
            return radian - FPI_BY_TWO;
        }
        return radian;
    }

    public static float radianAngleDiff(float a, float b) {
//        a = a - b;
//        a = ((a + FPI) % FPI_BY_TWO) - FPI;
//        return a;
        float plainDif = (FRAD2DEG * (a - b)) % 360;
        float dif = Math.abs(plainDif) % 360;

        if (dif > 180) dif = 360 - dif;
        return FRAD * (dif) * ((plainDif >= 0 && plainDif <= 180) || (plainDif <= -180 && plainDif >= -360) ? 1 : -1);
    }
}
