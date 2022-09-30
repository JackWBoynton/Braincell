package bottomtextdanny.braincell.libraries.chart.steppy;

import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.base.function.TriFunction;
import bottomtextdanny.braincell.libraries.chart.steppy.data.*;
import bottomtextdanny.braincell.libraries.chart.steppy.iteration.TransientData;
import bottomtextdanny.braincell.libraries.chart.steppy.step.BooleanStep;
import bottomtextdanny.braincell.libraries.chart.steppy.step.Step;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import bottomtextdanny.braincell.tables.BCStepDataTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public class ChartBooleanSteps {
    public static final BooleanStep IS_AIR = blockStateContext(
            BlockBehaviour.BlockStateBase::isAir,
            null
    );
    public static final BooleanStep CAN_OCCLUDE = blockStateContext(
            BlockBehaviour.BlockStateBase::canOcclude,
            null
    );
    public static final BooleanStep HAS_BLOCK_ENTITY = blockStateContext(
            BlockBehaviour.BlockStateBase::hasBlockEntity,
            null
    );

    public static final BooleanStep IS_BURNING = blockStatePosLevelContext(
            BlockState::isBurning,
            null
    );

    public static final BooleanStep IS_SUFFOCATING = blockStatePosLevelContext(
            BlockBehaviour.BlockStateBase::isSuffocating,
            null
    );

    public static final BooleanStep IS_VIEW_BLOCKING = blockStatePosLevelContext(
            BlockBehaviour.BlockStateBase::isViewBlocking,
            null
    );

    public static final BooleanStep CAN_SURVIVE = blockStatePosLevelContext(
            BlockBehaviour.BlockStateBase::canSurvive,
            null
    );

    public static final BooleanStep HAS_POST_PROCESS = blockStatePosLevelContext(
            BlockBehaviour.BlockStateBase::hasPostProcess,
            null
    );

    public record ReachesThreshold(Step numberThreshold) implements BooleanStep {

        @Override
        public boolean invokeBoolean(Data data, ObjectFetcher metadata, TransientData preceding) {
            return preceding.getOrDefault(BCStepDataTypes.NUMBER.get()).doubleValue() >= numberThreshold.invokeCast(data, metadata, preceding, 1.0);
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    public record IsTag(Step tag) implements BooleanStep {

        @Override
        public boolean invokeBoolean(Data data, ObjectFetcher metadata, TransientData preceding) {
            BlockState blockState = preceding.getOrNull(BCStepDataTypes.BLOCK_STATE.get());

            if (blockState == null) return false;

            return blockState.is(tag.invokeCast(data, metadata, preceding, (TagKey<Block>)null));
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    public record IsBlock(Block block) implements BooleanStep {

        @Override
        public boolean invokeBoolean(Data data, ObjectFetcher metadata, TransientData preceding) {
            BlockState blockState = preceding.getOrNull(BCStepDataTypes.BLOCK_STATE.get());

            if (blockState == null) return false;

            return blockState.is(block);
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    private static BooleanStep blockStatePosLevelContext(TriFunction<BlockState, LevelReader, BlockPos, Boolean>  function,
                                                        Wrap<Serializer<BooleanStep>> serializer) {
        return new BooleanStep() {
            @Override
            public boolean invokeBoolean(Data data, ObjectFetcher metadata, TransientData preceding) {
                BlockState blockState = preceding.getOrNull(BCStepDataTypes.BLOCK_STATE.get());
                if (blockState == null) return false;
                Vec3 vec = preceding.getOrNull(BCStepDataTypes.VEC3.get());
                if (vec == null) return false;

                return function.apply(blockState, data.cast(ReadableLevelArgument.class).readableLevel(), preceding.block(vec));
            }

            @Override
            public Wrap<? extends Serializer<?>> serializer() {
                return serializer;
            }
        };
    }

    private static BooleanStep blockStateContext(Function<BlockState, Boolean> function,
                                                Wrap<Serializer<BooleanStep>> serializer) {
        return new BooleanStep() {
            @Override
            public boolean invokeBoolean(Data data, ObjectFetcher metadata, TransientData preceding) {
                BlockState blockState = preceding.getOrNull(BCStepDataTypes.BLOCK_STATE.get());
                if (blockState == null) return false;

                return function.apply(blockState);
            }

            @Override
            public Wrap<? extends Serializer<?>> serializer() {
                return serializer;
            }
        };
    }
}
