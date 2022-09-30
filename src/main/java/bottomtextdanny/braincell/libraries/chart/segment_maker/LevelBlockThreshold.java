package bottomtextdanny.braincell.libraries.chart.segment_maker;

import bottomtextdanny.braincell.libraries.chart.segment.SegmentData;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public interface LevelBlockThreshold extends LevelBlockPredicate, SegmentFloat {

    default boolean test(SegmentData data) {
        return test(data.level(), data.state(), data.blockPos());
    }

    @Override
    default boolean test(WorldGenLevel level, BlockState state, BlockPos blockPos) {
        return process(level, state, blockPos, 0.0F) >= 1.0F;
    }

    float process(WorldGenLevel level, BlockState state, BlockPos blockPos, float base);

    @Override
    default float process(SegmentData data, float base) {
        return process(data.level(), data.state(), data.blockPos(), base);
    }

    default LevelBlockPredicates.ThresholdConcat appendThreshold(LevelBlockThreshold test) {
        LevelBlockPredicates.ThresholdConcat concat = new LevelBlockPredicates.ThresholdConcat();
        concat.appendThreshold(this);
        concat.appendThreshold(test);
        return concat;
    }

    @Override
    default Wrap<? extends Serializer<? extends LevelBlockThreshold>> serializer() {
        return null;
    }
}
