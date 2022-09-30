package bottomtextdanny.braincell.libraries.chart.steppy.iteration;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public final class StepDataType<T> {
    private static int hashGetter = 0;
    private final int hash;
    private Supplier<T> defaultValueGetter;
    private T defaultValue;

    public StepDataType(Supplier<T> defaultValue) {
        super();
        this.defaultValueGetter = defaultValue;
        hash = hashGetter;
        hashGetter++;
    }

    public T defaultValue() {
        tryMemoizeDefaultValue();
        return defaultValue;
    }

    private void tryMemoizeDefaultValue() {
        if (defaultValueGetter != null) {
            defaultValue = defaultValueGetter.get();
            defaultValueGetter = null;
        }
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
