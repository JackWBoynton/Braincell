package bottomtextdanny.braincell.libraries.chart.segment_maker;

import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import bottomtextdanny.braincell.tables.BCSerializers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public final class SegmentPregenerations {
    private static final Cancel CANCEL = new Cancel();
    private static final Pass PASS = new Pass();

    public static Cancel spgCancel() {
        return CANCEL;
    }

    public static Pass spgPass() {
        return PASS;
    }

    public static Concat spgConcat() {
        return new Concat();
    }

    public static If spgIf(LevelBlockPredicate predicate, SegmentPregeneration statement) {
        return new If(predicate, statement);
    }

    public static Either spgEither(LevelBlockPredicate predicate,
                                     SegmentPregeneration positive,
                                     SegmentPregeneration negative) {
        return new Either(predicate, positive, negative);
    }

    public static IfNull spgIfNull(SegmentPregeneration alternative) {
        return new IfNull(alternative);
    }

    public static Height spgHeight(Heightmap.Types type) {
        return new Height(type);
    }

    public static FindSurface spgFindSurface(int upRange, int downRange,
                                          LevelBlockPredicate buriedPredicate) {
        return new FindSurface(buriedPredicate, upRange, downRange);
    }

    public record Cancel() implements SegmentPregeneration {

        @Nullable
        @Override
        public BlockPos process(WorldGenLevel level, BlockPos blockPos) {
            return null;
        }

        @Override
        public Wrap<? extends Serializer<? extends SegmentPregeneration>> serializer() {
            return BCSerializers.SEGMENT_PREGEN_CANCEL;
        }
    }

    public record Pass() implements SegmentPregeneration {

        @Nullable
        @Override
        public BlockPos process(WorldGenLevel level, BlockPos blockPos) {
            return blockPos;
        }

        @Override
        public Wrap<? extends Serializer<? extends SegmentPregeneration>> serializer() {
            return BCSerializers.SEGMENT_PREGEN_PASS;
        }
    }

    public static class Concat implements SegmentPregeneration {
        private final List<SegmentPregeneration> list = new LinkedList<>();

        @Nullable
        @Override
        public BlockPos process(WorldGenLevel level, BlockPos blockPos) {
            for (SegmentPregeneration pregen : list) {
                blockPos = pregen.process(level, blockPos);
                if (blockPos == null) return null;
            }
            return blockPos;
        }

        @Override
        public SegmentPregenerations.Concat append(SegmentPregeneration decorator) {
            list.add(decorator);
            return this;
        }

        public void iterate(Consumer<SegmentPregeneration> consumer) {
            list.forEach(consumer);
        }

        @Override
        public Wrap<? extends Serializer<? extends SegmentPregeneration>> serializer() {
            return BCSerializers.SEGMENT_PREGEN_CONCAT;
        }
    }

    public record If(LevelBlockPredicate conditional, SegmentPregeneration positiveOutcome) implements SegmentPregeneration {

        @Nullable
        @Override
        public BlockPos process(WorldGenLevel level, BlockPos blockPos) {
            return conditional.test(level, blockPos)
                    ? positiveOutcome.process(level, blockPos)
                    : null;
        }

        @Override
        public Wrap<? extends Serializer<? extends SegmentPregeneration>> serializer() {
            return BCSerializers.SEGMENT_PREGEN_IF;
        }
    }

    public record Either(LevelBlockPredicate conditional,
                         SegmentPregeneration positiveOutcome,
                         SegmentPregeneration negativeOutcome) implements SegmentPregeneration {

        @Nullable
        @Override
        public BlockPos process(WorldGenLevel level, BlockPos blockPos) {
            return conditional.test(level, blockPos)
                    ? positiveOutcome.process(level, blockPos)
                    : negativeOutcome.process(level, blockPos);
        }

        @Override
        public Wrap<? extends Serializer<? extends SegmentPregeneration>> serializer() {
            return BCSerializers.SEGMENT_PREGEN_EITHER;
        }
    }

    public record IfNull(SegmentPregeneration alternative) implements SegmentPregeneration {

        @Nullable
        @Override
        public BlockPos process(WorldGenLevel level, BlockPos blockPos) {
            return blockPos == null ? alternative.process(level, BlockPos.ZERO) : blockPos;
        }

        @Override
        public Wrap<? extends Serializer<? extends SegmentPregeneration>> serializer() {
            return BCSerializers.SEGMENT_PREGEN_IF_NULL;
        }
    }

    public record Height(Heightmap.Types heightType) implements SegmentPregeneration {

        @Override
        public BlockPos process(WorldGenLevel level, BlockPos blockPos) {
            return new BlockPos(blockPos.getX(), level.getHeight(heightType, blockPos.getX(), blockPos.getZ()), blockPos.getZ());
        }

        @Override
        public Wrap<? extends Serializer<? extends SegmentPregeneration>> serializer() {
            return BCSerializers.SEGMENT_PREGEN_HEIGHT;
        }
    }

    public record FindSurface(LevelBlockPredicate buriedPredicate,
                              int upRange,
                              int downRange) implements SegmentPregeneration {

        @Override
        public BlockPos process(WorldGenLevel level, BlockPos blockPos) {
            BlockPos.MutableBlockPos mu = new BlockPos.MutableBlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            BlockState start = level.getBlockState(blockPos);

            if (buriedPredicate.test(level, start, blockPos)) {
                for (int i = 0; i < upRange; i++) {
                    mu.move(0, 1, 0);
                    if (!buriedPredicate.test(level, level.getBlockState(mu), mu))
                        return new BlockPos(mu);
                }
            } else {
                for (int i = 0; i < downRange; i++) {
                    mu.move(0, -1, 0);
                    if (buriedPredicate.test(level, level.getBlockState(mu), mu)) {
                        return mu.above();
                    }
                }
            }

            return new BlockPos(mu);
        }

        @Override
        public Wrap<? extends Serializer<? extends SegmentPregeneration>> serializer() {
            return BCSerializers.SEGMENT_PREGEN_FIND_SURFACE;
        }
    }
}
