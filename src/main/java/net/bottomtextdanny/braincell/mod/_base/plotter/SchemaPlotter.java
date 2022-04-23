package net.bottomtextdanny.braincell.mod._base.plotter;

import net.bottomtextdanny.braincell.mod._base.plotter.iterator.MutablePlotterData;
import net.bottomtextdanny.braincell.mod._base.plotter.iterator.PlotterData;
import net.bottomtextdanny.braincell.mod._base.plotter.iterator.PlotterIterator;
import net.bottomtextdanny.braincell.mod._base.plotter.schema.FlagsEntry;
import net.bottomtextdanny.braincell.mod._base.plotter.schema.Schema;
import net.bottomtextdanny.braincell.mod._base.plotter.schema.SchemaGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;
import java.util.random.RandomGenerator;

public class SchemaPlotter extends SpecialPlotter<SchemaPlotter.Data> {
    private final SchemaGetter list;

    public SchemaPlotter(LevelAccessor level, SchemaGetter list) {
        super(level);
        this.list = list;
    }

    @Override
    public void makeOwnThing(BlockPos pos, PlotterIterator<Data> iterator, Rotation rotation) {
        SchemaPlotter.MuData data = new MuData(this, null, null, null, null, NT_RANDOM);

        Schema schema = this.list.make(this.level, NT_RANDOM);
        schema.iterate((lp, p, f) -> {
            lp = PlotterRotator.rotatedOf(rotation, lp);
            BlockPos worldPos = pos.offset(lp);
            BlockState state = this.level.getBlockState(worldPos);
            data.setEntry(f);
            data.setPropertiesInjector(blockState -> {
                int propertiesSize = p.size();
                for (int i = 0; i < propertiesSize; i += 2) {
                    blockState = schema.tryInferPropertyValue(p.getInt(i), p.getInt(i + 1), blockState);
                }
                return blockState;
            });
            data.setState(state);
            data.setOriginalState(state);
            data.setLocalPos(lp);
            data.setBlockPos(worldPos);
            iterator.accept(data);
            this.level.setBlock(worldPos, PlotterRotator.rotateBlockState(rotation, data.state()), 3);
        });
    }

    public interface Data extends PlotterData {
        BlockState infer(BlockState blockState);

        FlagsEntry flags();
    }

    private static class MuData extends MutablePlotterData implements SchemaPlotter.Data {
        private static final Function<BlockState, BlockState> IDENTITY = Function.identity();
        private Function<BlockState, BlockState> inferProperties = IDENTITY;
        private FlagsEntry entry;

        public MuData(Plotter<?> plotter, BlockState state, BlockState originalState, BlockPos blockPos, BlockPos localPos, RandomGenerator random) {
            super(plotter, state, originalState, blockPos, localPos, random);
        }

        @Override
        public BlockState infer(BlockState blockState) {
            return this.inferProperties.apply(blockState);
        }

        public void setPropertiesInjector(Function<BlockState, BlockState> inferProperties) {
            this.inferProperties = inferProperties;
        }

        @Override
        public FlagsEntry flags() {
            return this.entry;
        }

        public void setEntry(FlagsEntry entry) {
            this.entry = entry;
        }
    }
}
