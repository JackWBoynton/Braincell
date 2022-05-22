package bottomtextdanny.braincell.base;

import java.util.List;
import java.util.random.RandomGenerator;

public class BCRandomUtil {

    public static <T> T randomOf(T[] array, RandomGenerator randomizer) {
        return array[randomizer.nextInt(array.length)];
    }

    public static <T> T randomOf(List<T> list, RandomGenerator randomizer) {
        return list.get(randomizer.nextInt(list.size()));
    }
}
