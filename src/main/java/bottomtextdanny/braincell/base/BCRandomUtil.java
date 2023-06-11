package bottomtextdanny.braincell.base;

import java.util.List;
import java.util.random.RandomGenerator;

public class BCRandomUtil {
   public static Object randomOf(Object[] array, RandomGenerator randomizer) {
      return array[randomizer.nextInt(array.length)];
   }

   public static Object randomOf(List list, RandomGenerator randomizer) {
      return list.get(randomizer.nextInt(list.size()));
   }
}
