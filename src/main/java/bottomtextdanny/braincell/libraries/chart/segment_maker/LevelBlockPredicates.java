package bottomtextdanny.braincell.libraries.chart.segment_maker;

import bottomtextdanny.braincell.libraries.chart.help.Distance3;
import bottomtextdanny.braincell.libraries.chart.help.GridSampler;
import bottomtextdanny.braincell.libraries.chart.help.GridWarper;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import bottomtextdanny.braincell.tables.BCSerializers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public final class LevelBlockPredicates {
    private static final RandomSource RANDOM_SOURCE = RandomSource.create(69L);
    private static final LevelBlockPredicate NO_LEVEL_BLOCK_PREDICATE = new LevelBlockPredicate() {

        @Override
        public boolean test(WorldGenLevel level, BlockState state, BlockPos blockPos) {
            return false;
        }

        @Override
        public Wrap<? extends Serializer<? extends LevelBlockPredicate>> serializer() {
            return BCSerializers.LEVEL_BLOCK_PREDICATE_CANCEL;
        }
    };
    private static final IsAir IS_AIR = new IsAir();
    private static final HasBlockEntity HAS_BLOCK_ENTITY = new HasBlockEntity();
    private static final IsEmptyFluid IS_EMPTY_FLUID = new IsEmptyFluid();
    private static final IsFluidSource IS_FLUID_SOURCE = new IsFluidSource();
    private static final IsSolidRender IS_SOLID_RENDER = new IsSolidRender();
    private static final CanSurvive CAN_SURVIVE = new CanSurvive();
    private static final IsBurning IS_BURNING = new IsBurning();
    private static final CanOcclude CAN_OCCLUDE = new CanOcclude();

    public static LevelBlockPredicate lbpCancel() {
        return NO_LEVEL_BLOCK_PREDICATE;
    }

    public static Concat lbpConcat() {
        return new Concat();
    }

    public static ConcatOred lbpConcatOred() {
        return new ConcatOred();
    }

    public static Either lbpEither(LevelBlockPredicate statement,
                                   LevelBlockPredicate positive,
                                   LevelBlockPredicate negative) {
        return new Either(statement, positive, negative);
    }

    public static OrElse lbpOrElse(LevelBlockPredicate statement,
                                   LevelBlockPredicate negative) {
        return new OrElse(statement, negative);
    }

    public static Negate lbpNegate(LevelBlockPredicate predicate) {
        return new Negate(predicate);
    }

    public static Random lbpRandom(float probability) {
        return new Random(probability);
    }

    public static IsAir lbpIsAir() {
        return IS_AIR;
    }

    public static HasBlockEntity lbpHasBlockEntity() {
        return HAS_BLOCK_ENTITY;
    }

    public static IsFluid lbpIsFluid(Fluid fluid) {
        return new IsFluid(fluid);
    }

    public static IsEmptyFluid lbpIsEmptyFluid() {
        return IS_EMPTY_FLUID;
    }

    public static IsFluidSource lbpIsFluidSource() {
        return IS_FLUID_SOURCE;
    }

    public static IsSolidRender lbpIsSolidRender() {
        return IS_SOLID_RENDER;
    }

    public static CanSurvive lbpCanSurvive() {
        return CAN_SURVIVE;
    }

    public static IsBurning lbpIsBurning() {
        return IS_BURNING;
    }

    public static IsFireSource lbpIsFireSource(Direction direction) {
        return new IsFireSource(direction);
    }

    public static IsFlammable lbpIsFlammable(Direction direction) {
        return new IsFlammable(direction);
    }

    public static CanOcclude lbpCanOcclude() {
        return CAN_OCCLUDE;
    }

    public static Offset lbpOffset(BlockPos offset, LevelBlockPredicate predicate) {
        return new Offset(offset, predicate);
    }

    public static HasTag lbpHasTag(TagKey<Block> tag) {
        return new HasTag(tag);
    }

    public static IsBlock lbpIs(Block block) {
        return new IsBlock(block);
    }

    public static RandomRange lbtRandomRange() {
        return new RandomRange();
    }

    public static Scale lbtScale(float scalar) {
        return new Scale(scalar);
    }

    public static ScaleBy lbtScaleBy(LevelBlockThreshold scalar) {
        return new ScaleBy(scalar);
    }

    public static Add lbtAdd(float addition) {
        return new Add(addition);
    }

    public static AddOther lbtAddOther(LevelBlockThreshold addition) {
        return new AddOther(addition);
    }

    public static Layer lbtLayer(GridSampler sampler, GridWarper warper) {
        return new Layer(sampler, warper);
    }

    public static Distance lbtDistance(Vec3i pivot, Distance3 calculator, float magnitude) {
        return new Distance(pivot, calculator, magnitude);
    }

    public static DistanceInverted lbtDistanceInv(Vec3i pivot, Distance3 calculator, float magnitude) {
        return new DistanceInverted(pivot, calculator, magnitude);
    }

    public static class Concat implements LevelBlockPredicate {
        private final List<LevelBlockPredicate> list = new LinkedList<>();

        @Override
        public LevelBlockPredicates.Concat lbpAppend(LevelBlockPredicate predicate) {
            list.add(predicate);
            return this;
        }

        @Override
        public boolean test(WorldGenLevel level, BlockState state, BlockPos blockPos) {
            return list.stream().allMatch(predicate -> predicate.test(level, state, blockPos));
        }

        public void iterate(Consumer<LevelBlockPredicate> consumer) {
            list.forEach(consumer);
        }

        @Override
        public Wrap<? extends Serializer<? extends LevelBlockPredicate>> serializer() {
            return BCSerializers.LEVEL_BLOCK_PREDICATE_CONCAT;
        }
    }

    public static class ConcatOred implements LevelBlockPredicate {
        private final List<LevelBlockPredicate> list = new LinkedList<>();

        @Override
        public LevelBlockPredicates.ConcatOred lbpOr(LevelBlockPredicate predicate) {
            list.add(predicate);
            return this;
        }

        @Override
        public boolean test(WorldGenLevel level, BlockState state, BlockPos blockPos) {
            return list.stream().anyMatch(predicate -> predicate.test(level, state, blockPos));
        }

        public void iterate(Consumer<LevelBlockPredicate> consumer) {
            list.forEach(consumer);
        }

        @Override
        public Wrap<? extends Serializer<? extends LevelBlockPredicate>> serializer() {
            return BCSerializers.LEVEL_BLOCK_PREDICATE_CONCAT_ORED;
        }
    }

    public record Either(LevelBlockPredicate statement,
                         LevelBlockPredicate positive,
                         LevelBlockPredicate negative) implements LevelBlockPredicate {

        @Override
        public boolean test(WorldGenLevel level, BlockState state, BlockPos blockPos) {
            return statement.test(level, state, blockPos)
                    ? positive.test(level, state, blockPos)
                    : negative.test(level, state, blockPos);
        }

        @Override
        public Wrap<? extends Serializer<? extends LevelBlockPredicate>> serializer() {
            return BCSerializers.LEVEL_BLOCK_PREDICATE_EITHER;
        }
    }

    public record OrElse(LevelBlockPredicate statement,
                         LevelBlockPredicate negative) implements LevelBlockPredicate {

        @Override
        public boolean test(WorldGenLevel level, BlockState state, BlockPos blockPos) {
            return statement.test(level, state, blockPos) || negative.test(level, state, blockPos);
        }

        @Override
        public Wrap<? extends Serializer<? extends LevelBlockPredicate>> serializer() {
            return BCSerializers.LEVEL_BLOCK_PREDICATE_OR_ELSE;
        }
    }

    public record Negate(LevelBlockPredicate predicate) implements LevelBlockPredicate {
        @Override
        public boolean test(WorldGenLevel level, BlockState state, BlockPos blockPos) {
            return !predicate.test(level, state, blockPos);
        }

        @Override
        public Wrap<? extends Serializer<? extends LevelBlockPredicate>> serializer() {
            return BCSerializers.LEVEL_BLOCK_PREDICATE_NEGATE;
        }
    }

    public record Random(float probability) implements LevelBlockPredicate {
        @Override
        public boolean test(WorldGenLevel level, BlockState state, BlockPos blockPos) {
            return RANDOM_SOURCE.nextFloat() < probability;
        }

        @Override
        public Wrap<? extends Serializer<? extends LevelBlockPredicate>> serializer() {
            return BCSerializers.LEVEL_BLOCK_PREDICATE_RANDOM;
        }
    }

    public record IsAir() implements LevelBlockPredicate {
        @Override
        public boolean test(WorldGenLevel level, BlockState state, BlockPos blockPos) {
            return state.isAir();
        }

        @Override
        public Wrap<? extends Serializer<? extends LevelBlockPredicate>> serializer() {
            return BCSerializers.LEVEL_BLOCK_PREDICATE_IS_AIR;
        }
    }

    public record HasBlockEntity() implements LevelBlockPredicate {
        @Override
        public boolean test(WorldGenLevel level, BlockState state, BlockPos blockPos) {
            return state.hasBlockEntity();
        }

        @Override
        public Wrap<? extends Serializer<? extends LevelBlockPredicate>> serializer() {
            return BCSerializers.LEVEL_BLOCK_PREDICATE_HAS_BLOCK_ENTITY;
        }
    }

    public record IsFluid(Fluid fluid) implements LevelBlockPredicate {
        @Override
        public boolean test(WorldGenLevel level, BlockState state, BlockPos blockPos) {
            return state.getFluidState().is(fluid);
        }

        @Override
        public Wrap<? extends Serializer<? extends LevelBlockPredicate>> serializer() {
            return BCSerializers.LEVEL_BLOCK_PREDICATE_IS_FLUID;
        }
    }

    public record IsEmptyFluid() implements LevelBlockPredicate {
        @Override
        public boolean test(WorldGenLevel level, BlockState state, BlockPos blockPos) {
            return state.getFluidState().isEmpty();
        }

        @Override
        public Wrap<? extends Serializer<? extends LevelBlockPredicate>> serializer() {
            return BCSerializers.LEVEL_BLOCK_PREDICATE_IS_EMPTY_FLUID;
        }
    }

    public record IsFluidSource() implements LevelBlockPredicate {
        @Override
        public boolean test(WorldGenLevel level, BlockState state, BlockPos blockPos) {
            return state.getFluidState().isSource();
        }

        @Override
        public Wrap<? extends Serializer<? extends LevelBlockPredicate>> serializer() {
            return BCSerializers.LEVEL_BLOCK_PREDICATE_IS_SOURCE_FLUID;
        }
    }

    public record IsSolidRender() implements LevelBlockPredicate {
        @Override
        public boolean test(WorldGenLevel level, BlockState state, BlockPos blockPos) {
            return state.isSolidRender(level, blockPos);
        }

        @Override
        public Wrap<? extends Serializer<? extends LevelBlockPredicate>> serializer() {
            return BCSerializers.LEVEL_BLOCK_PREDICATE_IS_SOLID_RENDER;
        }
    }

    public record CanSurvive() implements LevelBlockPredicate {
        @Override
        public boolean test(WorldGenLevel level, BlockState state, BlockPos blockPos) {
            return state.canSurvive(level, blockPos);
        }

        @Override
        public Wrap<? extends Serializer<? extends LevelBlockPredicate>> serializer() {
            return BCSerializers.LEVEL_BLOCK_PREDICATE_CAN_SURVIVE;
        }
    }

    public record IsBurning() implements LevelBlockPredicate {
        @Override
        public boolean test(WorldGenLevel level, BlockState state, BlockPos blockPos) {
            return state.isBurning(level, blockPos);
        }

        @Override
        public Wrap<? extends Serializer<? extends LevelBlockPredicate>> serializer() {
            return BCSerializers.LEVEL_BLOCK_PREDICATE_IS_BURNING;
        }
    }

    public record IsFireSource(Direction direction) implements LevelBlockPredicate {
        @Override
        public boolean test(WorldGenLevel level, BlockState state, BlockPos blockPos) {
            return state.isFireSource(level, blockPos, direction);
        }

        @Override
        public Wrap<? extends Serializer<? extends LevelBlockPredicate>> serializer() {
            return BCSerializers.LEVEL_BLOCK_PREDICATE_IS_FIRE_SOURCE;
        }
    }

    public record IsFlammable(Direction direction) implements LevelBlockPredicate {
        @Override
        public boolean test(WorldGenLevel level, BlockState state, BlockPos blockPos) {
            return state.isFlammable(level, blockPos, direction);
        }

        @Override
        public Wrap<? extends Serializer<? extends LevelBlockPredicate>> serializer() {
            return BCSerializers.LEVEL_BLOCK_PREDICATE_IS_FLAMMABLE;
        }
    }

    public record CanOcclude() implements LevelBlockPredicate {
        @Override
        public boolean test(WorldGenLevel level, BlockState state, BlockPos blockPos) {
            return state.canOcclude();
        }

        @Override
        public Wrap<? extends Serializer<? extends LevelBlockPredicate>> serializer() {
            return BCSerializers.LEVEL_BLOCK_PREDICATE_CAN_OCCLUDE;
        }
    }

    public record Offset(BlockPos offset, LevelBlockPredicate conditional) implements LevelBlockPredicate {
        @Override
        public boolean test(WorldGenLevel level, BlockState state, BlockPos blockPos) {
            blockPos = blockPos.offset(offset);
            return conditional.test(level, level.getBlockState(blockPos), blockPos);
        }

        @Override
        public Wrap<? extends Serializer<? extends LevelBlockPredicate>> serializer() {
            return BCSerializers.LEVEL_BLOCK_PREDICATE_OFFSET;
        }
    }

    public record HasTag(TagKey<Block> tag) implements LevelBlockPredicate {
        @Override
        public boolean test(WorldGenLevel level, BlockState state, BlockPos blockPos) {
            return state.getBlock().builtInRegistryHolder().is(tag);
        }

        @Override
        public Wrap<? extends Serializer<? extends LevelBlockPredicate>> serializer() {
            return BCSerializers.LEVEL_BLOCK_PREDICATE_HAS_TAG;
        }
    }

    public record IsBlock(Block block) implements LevelBlockPredicate {
        @Override
        public boolean test(WorldGenLevel level, BlockState state, BlockPos blockPos) {
            return state.getBlock() == block;
        }

        @Override
        public Wrap<? extends Serializer<? extends LevelBlockPredicate>> serializer() {
            return BCSerializers.LEVEL_BLOCK_PREDICATE_IS_BLOCK;
        }
    }

    public static class ThresholdConcat implements LevelBlockThreshold {
        private final List<LevelBlockThreshold> list = new LinkedList<>();

        @Override
        public float process(WorldGenLevel level, BlockState state, BlockPos blockPos, float base) {
            for (LevelBlockThreshold segmentTest : list) {
                base = segmentTest.process(level, state, blockPos, base);
            }
            return base;
        }

        public ThresholdConcat appendThreshold(LevelBlockThreshold test) {
            list.add(test);
            return this;
        }

        public void iterate(Consumer<LevelBlockThreshold> consumer) {
            list.forEach(consumer);
        }

        @Override
        public Wrap<? extends Serializer<ThresholdConcat>> serializer() {
            return BCSerializers.LEVEL_BLOCK_THRESHOLD_CONCAT;
        }
    }

    public record RandomRange() implements LevelBlockThreshold {

        @Override
        public float process(WorldGenLevel level, BlockState state, BlockPos blockPos, float base) {
            return base + RANDOM_SOURCE.nextFloat();
        }

        @Override
        public Wrap<? extends Serializer<RandomRange>> serializer() {
            return BCSerializers.LEVEL_BLOCK_THRESHOLD_RANDOM_RANGE;
        }
    }

    public record Scale(float scalar) implements LevelBlockThreshold {

        @Override
        public float process(WorldGenLevel level, BlockState state, BlockPos blockPos, float base) {
            return base * scalar;
        }

        @Override
        public Wrap<Serializer<Scale>> serializer() {
            return BCSerializers.LEVEL_BLOCK_THRESHOLD_SCALE;
        }
    }

    public record ScaleBy(LevelBlockThreshold scalar) implements LevelBlockThreshold {

        @Override
        public float process(WorldGenLevel level, BlockState state, BlockPos blockPos, float base) {
            return base * scalar.process(level, state, blockPos, base);
        }

        @Override
        public Wrap<Serializer<ScaleBy>> serializer() {
            return BCSerializers.LEVEL_BLOCK_THRESHOLD_SCALE_BY;
        }
    }

    public record Add(float addition) implements LevelBlockThreshold {

        @Override
        public float process(WorldGenLevel level, BlockState state, BlockPos blockPos, float base) {
            return base + addition;
        }

        @Override
        public Wrap<Serializer<Add>> serializer() {
            return BCSerializers.LEVEL_BLOCK_THRESHOLD_ADD;
        }
    }

    public record AddOther(LevelBlockThreshold addition) implements LevelBlockThreshold {

        @Override
        public float process(WorldGenLevel level, BlockState state, BlockPos blockPos, float base) {
            return base * addition.process(level, state, blockPos, base);
        }

        @Override
        public Wrap<Serializer<AddOther>> serializer() {
            return BCSerializers.LEVEL_BLOCK_THRESHOLD_ADD_OTHER;
        }
    }

    public record Layer(GridSampler sampler, GridWarper definition) implements LevelBlockThreshold {

        @Override
        public float process(WorldGenLevel level, BlockState state, BlockPos blockPos, float base) {
            return base + sampler.value(definition.warp(Vec3.atCenterOf(blockPos)));
        }

        @Override
        public Wrap<Serializer<Layer>> serializer() {
            return BCSerializers.LEVEL_BLOCK_THRESHOLD_LAYER;
        }
    }

    public record Distance(Vec3i pivot, Distance3 calculator, float magnitude) implements LevelBlockThreshold {

        @Override
        public float process(WorldGenLevel level, BlockState state, BlockPos blockPos, float base) {
            return base + (float)calculator.distance(pivot.getX(), pivot.getY(), pivot.getZ(), blockPos.getX(), blockPos.getY(), blockPos.getZ()) / magnitude;
        }

        @Override
        public Wrap<Serializer<Distance>> serializer() {
            return BCSerializers.LEVEL_BLOCK_THRESHOLD_DISTANCE;
        }
    }

    public record DistanceInverted(Vec3i pivot, Distance3 calculator, float magnitude) implements LevelBlockThreshold {

        @Override
        public float process(WorldGenLevel level, BlockState state, BlockPos blockPos, float base) {
            return base + 1.0F - (float)calculator.distance(pivot.getX(), pivot.getY(), pivot.getZ(), blockPos.getX(), blockPos.getY(), blockPos.getZ()) / magnitude;
        }

        @Override
        public Wrap<Serializer<DistanceInverted>> serializer() {
            return BCSerializers.LEVEL_BLOCK_THRESHOLD_DISTANCE_INVERTED;
        }
    }
}
