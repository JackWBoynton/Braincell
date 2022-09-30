package bottomtextdanny.braincell.libraries.chart.segment_maker;

import bottomtextdanny.braincell.base.pair.Pair;
import bottomtextdanny.braincell.libraries.chart.segment.SchemaSegment;
import bottomtextdanny.braincell.libraries.chart.segment.SegmentData;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import bottomtextdanny.braincell.tables.BCSerializers;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class SegmentDecorators {
    private static final SegmentDecorator NO_SEGMENT_DECORATOR = new SegmentDecorator() {

        @Override
        public void accept(SegmentData data) {}

        @Override
        public Wrap<? extends Serializer<? extends SegmentDecorator>> serializer() {
            return BCSerializers.SEGMENT_DECORATOR_BLANK;
        }
    };

    public static SegmentDecorator sdBlank() {
        return NO_SEGMENT_DECORATOR;
    }

    public static Concat sdConcat() {
        return new Concat();
    }

    public static If sdIf(SegmentPredicate conditional, SegmentDecorator function) {

//        sdByFirstFlag()
//                .entry(0, sdPlace(Blocks.MOSS_BLOCK))
//                .entry(1,
//                        sdEither(
//                                lbpCanOcclude().lbpAppend(lbpIsSolidRender()),
//                                sdByRange(
//                                        lbtLayer(gsSimplex().scale(0.2F), gw)
//                                                .appendThreshold()
//                                )
//                                        .entry(0.12F, sdPlace(Blocks.OAK_PLANKS))
//                                        .entry(0.42F, sdPlace(Blocks.)),
//                                sdPlace(Blocks.OAK_PLANKS)
//                        )
//                );
        return new If(conditional, function);
    }

    public static Either sdEither(SegmentPredicate conditional,
                                  SegmentDecorator positive,
                                  SegmentDecorator negative) {
        return new Either(conditional, positive, negative);
    }

    public static Place sdPlace(Block block) {
        return new Place(block);
    }

    public static PlaceBlockState sdPlaceBlockState(BlockState blockState) {
        return new PlaceBlockState(blockState);
    }

    public static PlaceInjected sdPlaceInjected(Block block) {
        return new PlaceInjected(block);
    }

    public static ByFlag sdByFlag(int index) {
        return new ByFlag(index);
    }

    public static ByFirstFlag sdByFirstFlag() {
        return new ByFirstFlag();
    }

    public static ByRange sdByRange(SegmentFloat valueMapper) {
        return new ByRange(valueMapper);
    }

    public static class Concat implements SegmentDecorator {
        private final List<SegmentDecorator> list = new LinkedList<>();

        @Override
        public void accept(SegmentData data) {
            list.forEach(decorator -> decorator.accept(data));
        }

        @Override
        public SegmentDecorators.Concat append(SegmentDecorator decorator) {
            list.add(decorator);
            return this;
        }

        public void iterate(Consumer<SegmentDecorator> consumer) {
            list.forEach(consumer);
        }

        @Override
        public Wrap<? extends Serializer<? extends SegmentDecorator>> serializer() {
            return BCSerializers.SEGMENT_DECORATOR_CONCAT;
        }
    }

    public record If(SegmentPredicate conditional, SegmentDecorator positiveOutcome) implements SegmentDecorator {

        @Override
        public void accept(SegmentData data) {
            if (conditional.test(data)) positiveOutcome.accept(data);
        }

        @Override
        public Wrap<? extends Serializer<? extends SegmentDecorator>> serializer() {
            return BCSerializers.SEGMENT_DECORATOR_IF;
        }
    }

    public record Either(SegmentPredicate conditional,
                         SegmentDecorator positive,
                         SegmentDecorator negative) implements SegmentDecorator {

        @Override
        public void accept(SegmentData data) {
            if (conditional.test(data)) positive.accept(data);
            else negative.accept(data);
        }

        @Override
        public Wrap<? extends Serializer<? extends SegmentDecorator>> serializer() {
            return BCSerializers.SEGMENT_DECORATOR_EITHER;
        }
    }

    public record Place(Block state) implements SegmentDecorator {

        @Override
        public void accept(SegmentData data) {
            data.setState(state.defaultBlockState());
        }

        @Override
        public Wrap<? extends Serializer<? extends SegmentDecorator>> serializer() {
            return BCSerializers.SEGMENT_DECORATOR_PLACE;
        }
    }

    public record PlaceBlockState(BlockState state) implements SegmentDecorator {

        @Override
        public void accept(SegmentData data) {
            data.setState(state);
        }

        @Override
        public Wrap<? extends Serializer<? extends SegmentDecorator>> serializer() {
            return BCSerializers.SEGMENT_DECORATOR_PLACE_BLOCKSTATE;
        }
    }

    public record PlaceInjected(Block block) implements SegmentDecorator {

        @Override
        public void accept(SegmentData data) {
            if (data instanceof SchemaSegment.Data schemaData)
                schemaData.setState(schemaData.infer(block.defaultBlockState()));
        }

        @Override
        public Wrap<? extends Serializer<? extends SegmentDecorator>> serializer() {
            return BCSerializers.SEGMENT_DECORATOR_PLACE_INJECTED;
        }
    }

    public static class ByRange implements SegmentDecorator {
        private final LinkedList<Pair<Float, SegmentDecorator>> list = new LinkedList<>();
        private final SegmentFloat valueMapper;

        public ByRange(SegmentFloat valueMapper) {
            this.valueMapper = valueMapper;
        }

        public ByRange entry(float range, SegmentDecorator function) {
            if (list.isEmpty() || list.getLast().left() >= range) {
                list.add(Pair.of(range, function));
            }
            return this;
        }

        @Override
        public void accept(SegmentData data) {
            float point = valueMapper.process(data, 0);

            for (Pair<Float, SegmentDecorator> pair : list) {
                if (pair.left() > point) {
                    pair.right().accept(data);
                    break;
                }
            }
        }

        public SegmentFloat valueMapper() {
            return valueMapper;
        }

        public void iterate(Consumer<Pair<Float, SegmentDecorator>> consumer) {
            list.forEach(consumer);
        }

        @Override
        public Wrap<? extends Serializer<? extends SegmentDecorator>> serializer() {
            return BCSerializers.SEGMENT_DECORATOR_BY_RANGE;
        }
    }

    public static class ByFlag implements SegmentDecorator {
        private final Int2ObjectOpenHashMap<SegmentDecorator> map = new Int2ObjectOpenHashMap<>();
        private final int index;

        public ByFlag(int index) {
            this.index = index;
        }

        public ByFlag entry(int flag, SegmentDecorator function) {
            map.put(flag, function);
            return this;
        }

        @Override
        public void accept(SegmentData data) {
            if (!(data instanceof SchemaSegment.Data schemaData)) return;

            int flag = schemaData.flags().flagAt(index);
            if (map.containsKey(flag)) {
                map.get(flag).accept(data);
            }
        }

        public int index() {
            return index;
        }

        public void iterate(BiConsumer<Integer, SegmentDecorator> consumer) {
            map.forEach(consumer);
        }

        @Override
        public Wrap<? extends Serializer<? extends SegmentDecorator>> serializer() {
            return BCSerializers.SEGMENT_DECORATOR_BY_FLAG;
        }
    }

    public static class ByFirstFlag implements SegmentDecorator {
        private final Int2ObjectOpenHashMap<SegmentDecorator> map = new Int2ObjectOpenHashMap<>();

        public ByFirstFlag() {
        }

        public ByFirstFlag entry(int flag, SegmentDecorator function) {
            map.put(flag, function);
            return this;
        }

        @Override
        public void accept(SegmentData data) {
            if (!(data instanceof SchemaSegment.Data schemaData)) return;

            int flag = schemaData.flags().firstFlag();
            if (map.containsKey(flag)) {
                map.get(flag).accept(data);
            }
        }

        public void iterate(BiConsumer<Integer, SegmentDecorator> consumer) {
            map.forEach(consumer);
        }

        @Override
        public Wrap<? extends Serializer<? extends SegmentDecorator>> serializer() {
            return BCSerializers.SEGMENT_DECORATOR_BY_FIRST_FLAG;
        }
    }
}
