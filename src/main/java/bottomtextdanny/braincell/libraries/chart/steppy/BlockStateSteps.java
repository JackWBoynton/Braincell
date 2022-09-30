package bottomtextdanny.braincell.libraries.chart.steppy;

import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.libraries.chart.steppy.data.*;
import bottomtextdanny.braincell.libraries.chart.steppy.iteration.TransientData;
import bottomtextdanny.braincell.libraries.chart.steppy.step.BlockStateStep;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import bottomtextdanny.braincell.tables.BCStepDataTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public final class BlockStateSteps {

    @StepType(Type.ROOT)
    public record Fixed(BlockState blockState) implements BlockStateStep {

        @Override
        public BlockState invokeBlockState(Data data, ObjectFetcher metadata, TransientData preceding) {
            return blockState;
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.ROOT)
    public record FromBlock(Block block) implements BlockStateStep {

        @Override
        public BlockState invokeBlockState(Data data, ObjectFetcher metadata, TransientData preceding) {
            return block.defaultBlockState();
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @DataRequires(SchemaData.class)
    @StepType(Type.CONTEXT_FUNCTION)
    public record InjectSchemaProperties() implements BlockStateStep {
        public static final InjectSchemaProperties INSTANCE = new InjectSchemaProperties();

        @Override
        public BlockState invokeBlockState(Data data, ObjectFetcher metadata, TransientData preceding) {
            return data.cast(SchemaData.class).injectSchemaProperties(preceding.getOrDefault(BCStepDataTypes.BLOCK_STATE.get()));
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @DataRequires(LevelBlockStateArgument.class)
    @StepType(Type.ROOT)
    public record LevelArgument() implements BlockStateStep {
        public static final LevelArgument INSTANCE = new LevelArgument();

        @Override
        public BlockState invokeBlockState(Data data, ObjectFetcher metadata, TransientData preceding) {
            return data.cast(LevelBlockStateArgument.class).levelBlockState();
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }
}
