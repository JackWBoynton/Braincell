package bottomtextdanny.braincell.base;

import net.minecraft.util.RandomSource;

import java.util.List;

public class BCRandomUtil {

    public static <T> T randomOf(T[] array, RandomSource randomizer) {
        return array[randomizer.nextInt(array.length)];
    }

    public static <T> T randomOf(List<T> list, RandomSource randomizer) {
        return list.get(randomizer.nextInt(list.size()));
    }

    public static float ramble(double val, double deviationMagnitude, RandomSource randomizer) {
        return (float) (val - deviationMagnitude / 2 + randomizer.nextFloat() * deviationMagnitude);
    }

    public static float ramble(double val, double deviationMagnitude, RandomSource randomizer, FloatRandomPicker picker) {
        double div2mag = deviationMagnitude / 2;
        return picker.compute((float) (val - div2mag), (float) (val + div2mag), randomizer);
    }
}
