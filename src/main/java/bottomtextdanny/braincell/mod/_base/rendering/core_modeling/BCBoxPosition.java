package bottomtextdanny.braincell.mod._base.rendering.core_modeling;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public enum BCBoxPosition {
   LEFT_UP_FRONT(0, 1, 2),
   RIGHT_UP_FRONT(3, 1, 2),
   LEFT_DOWN_FRONT(0, 4, 2),
   RIGHT_DOWN_FRONT(3, 4, 2),
   LEFT_UP_BACK(0, 1, 5),
   RIGHT_UP_BACK(3, 1, 5),
   LEFT_DOWN_BACK(0, 4, 5),
   RIGHT_DOWN_BACK(3, 4, 5);

   public final int x;
   public final int y;
   public final int z;

   private BCBoxPosition(int x, int y, int z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   // $FF: synthetic method
   private static BCBoxPosition[] $values() {
      return new BCBoxPosition[]{LEFT_UP_FRONT, RIGHT_UP_FRONT, LEFT_DOWN_FRONT, RIGHT_DOWN_FRONT, LEFT_UP_BACK, RIGHT_UP_BACK, LEFT_DOWN_BACK, RIGHT_DOWN_BACK};
   }
}
