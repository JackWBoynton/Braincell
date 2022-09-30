package bottomtextdanny.braincell.libraries.chart.segment;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.chart.schema.FlagsEntry;
import bottomtextdanny.braincell.libraries.chart.schema.Schema;
import bottomtextdanny.braincell.libraries.chart.schema.SchemaMemo;
import bottomtextdanny.braincell.libraries.chart.segment_maker.SegmentDecorator;
import bottomtextdanny.braincell.libraries.registry.ContextualSerializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import bottomtextdanny.braincell.tables.BCContextualSerializers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class SchemaSegment extends Segment<SchemaSegment.Data> {
    public static final String SCHEMA_ID_LABEL = "schema_id";
    private final SchemaMemo schemaMemo;

    public SchemaSegment(SchemaMemo schema) {
        this.schemaMemo = schema;
    }

    public SchemaSegment(CompoundTag root, @Nullable SegmentAdmin admin) {
        super(root, admin);

        schemaMemo = Braincell.common().getSchemaManager().parseSchema(new ResourceLocation(root.getString(SCHEMA_ID_LABEL)));
    }

    @Override
    public CompoundTag childData() {
        CompoundTag tag = super.childData();

        tag.put(SCHEMA_ID_LABEL, StringTag.valueOf(schemaMemo.getLocation().toString()));

        return tag;
    }

    @Override
    public void construct(WorldGenLevel level, SegmentDecorator iterator, BlockPos pos, Rotation rotation) {
        MutableData data = new MutableData(level, null, null, null, null, NT_RANDOM);

        Schema schema = schemaMemo.getSchema();
        schema.iterate((lp, p, f) -> {
            lp = ChartRotator.rotatedOf(rotation, lp);
            BlockPos worldPos = pos.offset(lp);
            BlockState state = level.getBlockState(worldPos);
            data.setEntry(f);
            data.setPropertiesInjector(blockState -> {
                int propertiesSize = p.size();
                for (int i = 0; i < propertiesSize; i += 2) {
                    blockState = schema.tryInferPropertyValue(p.getInt(i), p.getInt(i + 1), blockState);
                }
                return blockState;
            });
            data.setOriginalState(state);
            data.setLocalPos(lp);
            data.setBlockPos(worldPos);
            iterator.accept(data);
            if (data.state() != null) level.setBlock(worldPos, ChartRotator.rotateBlockState(rotation, data.state()), 2);
        });
    }

    @Override
    public BoundingBox getDefaultLocalBoundingBox() {
        return schemaMemo.getSchema().getBuiltinBox();
    }

    @Override
    public Wrap<? extends ContextualSerializer<?, SegmentAdmin>> serializer() {
        return BCContextualSerializers.SCHEMA_SEGMENT;
    }

    public interface Data extends SegmentData {
        BlockState infer(BlockState blockState);

        FlagsEntry flags();
    }

    private static class MutableData extends MutableSegmentData implements Data {
        private static final Function<BlockState, BlockState> IDENTITY = Function.identity();
        private Function<BlockState, BlockState> inferProperties = IDENTITY;
        private FlagsEntry entry;

        public MutableData(WorldGenLevel level, BlockState state, BlockState originalState, BlockPos blockPos, BlockPos localPos, RandomSource random) {
            super(level, state, originalState, blockPos, localPos, random);
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
