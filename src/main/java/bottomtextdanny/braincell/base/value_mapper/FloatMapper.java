package bottomtextdanny.braincell.base.value_mapper;

import bottomtextdanny.braincell.base.FloatRandomPicker;
import net.minecraft.util.RandomSource;

@FunctionalInterface
public interface FloatMapper {
    float map(RandomSource random);

    static FloatMapper of(float value) {
        return r -> {
            return value;
        };
    }

    static FloatMapper from(float min, float max, FloatRandomPicker picker) {
        return r -> {
            return picker.compute(min, max, r);
        };
    }
}
