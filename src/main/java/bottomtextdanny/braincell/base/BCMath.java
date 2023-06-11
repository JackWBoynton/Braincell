package bottomtextdanny.braincell.base;

import net.minecraft.util.Mth;

public final class BCMath {
   public static final double SQRT_2 = 1.4142135623730951;
   public static final double RAD = 0.017453292519943295;
   public static final float FRAD = 0.017453292F;
   public static final float FPI = 3.1415927F;
   public static final double PI_BY_TWO = 6.283185307179586;
   public static final float FPI_BY_TWO = 6.2831855F;
   public static final double PI_HALF = 1.5707963267948966;
   public static final float FPI_HALF = 1.5707964F;
   public static final double RAD2DEG = 57.29577951308232;
   public static final float FRAD2DEG = 57.29578F;

   public static int roundUp(int val, int alignment) {
      int mod = val % alignment;
      return mod == 0 ? val : val + alignment - mod;
   }

   public static double dot3(double x, double y, double z) {
      return x * x + y * y + z * z;
   }

   public static float dot3(float x, float y, float z) {
      return x * x + y * y + z * z;
   }

   public static double dot2(double x, double y) {
      return x * x + y * y;
   }

   public static float dot2(float x, float y) {
      return x * x + y * y;
   }

   public static double fastpow(double val, double expo) {
      long tmp = Double.doubleToLongBits(val);
      long tmp2 = (long)(expo * (double)(tmp - 4606921280493453312L)) + 4606921280493453312L;
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
      return (un - 0.5F) * 2.0F;
   }

   public static float np2Zp(float un) {
      return un / 2.0F + 0.5F;
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

   public static float radianAngleDiff(float a, float b) {
      float plainDif = 57.29578F * (a - b) % 360.0F;
      float dif = Math.abs(plainDif) % 360.0F;
      if (dif > 180.0F) {
         dif = 360.0F - dif;
      }

      float r = 0.017453292F * dif * (float)((!(plainDif >= 0.0F) || !(plainDif <= 180.0F)) && (!(plainDif <= -180.0F) || !(plainDif >= -360.0F)) ? -1 : 1);
      return r;
   }
}
