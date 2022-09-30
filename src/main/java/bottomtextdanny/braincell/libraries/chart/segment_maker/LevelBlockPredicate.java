package bottomtextdanny.braincell.libraries.chart.segment_maker;

import bottomtextdanny.braincell.libraries.chart.segment.SegmentData;
import bottomtextdanny.braincell.libraries.registry.Serializable;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public interface LevelBlockPredicate extends Serializable, SegmentPredicate {

    boolean test(WorldGenLevel level, BlockState state, BlockPos blockPos);

    @Override
    default boolean test(SegmentData data) {
        return test(data.level(), data.state(), data.blockPos());
    }

    default boolean test(WorldGenLevel level, BlockPos blockPos) {
        return test(level, level.getBlockState(blockPos), blockPos);
    }

    default LevelBlockPredicates.Concat lbpAppend(LevelBlockPredicate other) {
        LevelBlockPredicates.Concat concat = new LevelBlockPredicates.Concat();
        concat.lbpAppend(this);
        concat.lbpAppend(other);
        return concat;
    }

    default LevelBlockPredicates.ConcatOred lbpOr(LevelBlockPredicate other) {
        LevelBlockPredicates.ConcatOred concat = new LevelBlockPredicates.ConcatOred();
        concat.lbpOr(this);
        concat.lbpOr(other);
        return concat;
    }

    default LevelBlockPredicates.Either lbpEither(LevelBlockPredicate positive,
                                                  LevelBlockPredicate negative) {
        return new LevelBlockPredicates.Either(this, positive, negative);
    }

    default LevelBlockPredicates.OrElse lbpOrElse(LevelBlockPredicate alternative) {
        return new LevelBlockPredicates.OrElse(this, alternative);
    }

    default LevelBlockPredicates.Negate lbpNegate() {
        return new LevelBlockPredicates.Negate(this);
    }

    @Override
    default Wrap<? extends Serializer<? extends LevelBlockPredicate>> serializer() {
        return null;
    }
}
