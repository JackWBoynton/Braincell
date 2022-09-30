package bottomtextdanny.braincell.libraries.chart.segment_maker;

import bottomtextdanny.braincell.libraries.registry.Serializable;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;

import javax.annotation.Nullable;

public interface SegmentPregeneration extends Serializable {

    @Nullable
    BlockPos process(WorldGenLevel level, BlockPos blockPos);

    default SegmentPregenerations.Concat append(SegmentPregeneration decorator) {
        SegmentPregenerations.Concat concat = new SegmentPregenerations.Concat();
        concat.append(this);
        concat.append(decorator);
        return concat;
    }

    @Override
    default Wrap<? extends Serializer<?>> serializer() {
        return null;
    }
}
