package bottomtextdanny.braincell.libraries.psyche.pos_finder;

import bottomtextdanny.braincell.base.FloatRandomPicker;
import bottomtextdanny.braincell.base.value_mapper.FloatMapper;
import bottomtextdanny.braincell.base.BCMath;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class MobPosProcessors {
    private static final MobPosProcessor<LivingEntity> AVOID = Util.make(
        () -> stack().generic(LivingEntity.class)
            .push(advanceHorizontalTarget(30 * BCMath.FRAD, FloatMapper.of(10)))
            .push(sample(10, stack()
                    .push(randomOffset(5, 4, 5))
                    .push(moveIf(Direction.DOWN, 16, MobPosPredicates.isSolid().negate()))
                    .push(offset(0, 1, 0)),
                MobPosComparators.compareWalkValue()
            )));

    public static <T> MobPosStackProcessor<T> stack() {
        return new MobPosStackProcessor<>();
    }

    public static <T> MobPosStackProcessor<T> stack(Class<T> infer) {
        return new MobPosStackProcessor<>();
    }

    public static <T> MobPosProcessor<T> randomOffset(int xAxis, int yAxis, int zAxis) {
        return (pos, mob, r, extra) -> pos.offset(
                r.nextInt(-xAxis, xAxis),
                r.nextInt(-yAxis, yAxis),
                r.nextInt(-zAxis, zAxis));
    }

    public static <T> MobPosProcessor<T> offset(int x, int y, int z) {
        return (pos, mob, r, extra) -> pos.offset(x, y, z);
    }

    public static <T extends LivingEntity> MobPosProcessor<T> yDiff() {
        return (pos, mob, r, target) -> pos.offset(0, (int)(target.getY() - mob.getY()), 0);
    }

    public static <T> MobPosProcessor<T> advanceHorizontal(FloatMapper radianAngleMapper,
                                                    FloatMapper stepMapper) {
        return (pos, mob, r, extra) -> {
            float radianAngle = radianAngleMapper.map(r);
            float step = stepMapper.map(r);

            return pos.offset(
                    BCMath.sin(radianAngle) * step,
                    0,
                    BCMath.cos(radianAngle) * step);
        };
    }

    public static <T> MobPosProcessor<T> advance(FloatMapper radianVerticalAngleMapper,
                                          FloatMapper radianHorizontalAngleMapper,
                                          FloatMapper stepMapper) {
        return (pos, mob, r, extra) -> {
            Vec3 vec = Vec3.directionFromRotation(
                    radianVerticalAngleMapper.map(r),
                    radianHorizontalAngleMapper.map(r))
                    .scale(stepMapper.map(r));

            return pos.offset(vec.x, vec.y, vec.z);
        };
    }

    public static <T> MobPosProcessor<T> advanceHorizontalForward(float deviationRad, FloatMapper stepMapper) {
        return advanceHorizontalForward(deviationRad, 0.0F, stepMapper);
    }

    public static <T> MobPosProcessor<T> advanceHorizontalForward(float deviationRad, float dirOffset, FloatMapper stepMapper) {
        return (pos, mob, r, target) -> {
            float v = mob.getYHeadRot() * BCMath.FRAD + dirOffset;

            float angleAwayFromTarget = FloatMapper.from(
                v - deviationRad,
                v + deviationRad,
                FloatRandomPicker.normal()).map(r);

            float step = stepMapper.map(r);

            return pos.offset(
                BCMath.sin(angleAwayFromTarget) * step,
                0,
                BCMath.cos(angleAwayFromTarget) * step);
        };
    }

    public static <T extends LivingEntity> MobPosProcessor<T> advanceHorizontalTarget(float deviationRad, FloatMapper stepMapper) {
        return advanceHorizontalTarget(deviationRad, 0.0F, stepMapper);
    }

    public static <T extends LivingEntity> MobPosProcessor<T> advanceHorizontalTarget(float deviationRad, float dirOffset, FloatMapper stepMapper) {
        return (pos, mob, r, target) -> {
            float targetRad = (float) Mth.atan2(target.getX() - mob.getX(), target.getZ() - mob.getZ()) + dirOffset;

            float angleAwayFromTarget = FloatMapper.from(
                targetRad - deviationRad,
                targetRad + deviationRad,
                FloatRandomPicker.normal()).map(r);

            float step = stepMapper.map(r);

            return pos.offset(
                BCMath.sin(angleAwayFromTarget) * step,
                0,
                BCMath.cos(angleAwayFromTarget) * step);
        };
    }

    public static <T> MobPosProcessor<T> find(int tries, MobPosProcessor<? super T> processor) {
        return (pos, mob, r, extra) -> {
            for (int i = 0; i < tries; i++) {
                BlockPos att = processor.compute(pos, mob, r, extra);
                if (att != null) return att;
            }

            return null;
        };
    }

    public static <T> MobPosProcessor<T> sample(int tries, MobPosProcessor<? super T> processor, BiFunction<Mob, ? super T, Comparator<BlockPos>> posComparatorGetter) {
        return (pos, mob, r, extra) -> {
            BlockPos newPos = null;
            Comparator<BlockPos> comparator = posComparatorGetter.apply(mob, extra);
            for (int i = 0; i < tries; i++) {
                BlockPos att = processor.compute(pos, mob, r, extra);
                if (newPos == null || (att != null && comparator.compare(newPos, att) >= 0)) {
                    newPos = att;
                }
            }

            return newPos;
        };
    }

    public static <T> MobPosProcessor<T> moveIf(Direction direction, int max, MobPosPredicate<? super T> predicate) {
        return (pos, mob, r, extra) -> {
            BlockPos.MutableBlockPos mu = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());

            for (int i = 0; i < max && predicate.test(mob, mu, extra); i++) {
                mu.move(direction);
            }

            return new BlockPos(mu);
        };
    }

    public static <T> MobPosProcessor<T> move(Direction direction, FloatMapper distance) {
        return (pos, mob, r, extra) -> {
            return pos.relative(direction, (int)distance.map(r));
        };
    }

    public static <T> MobPosProcessor<T> pred(MobPosPredicate<? super T> predicate) {
        return (pos, mob, r, extra) -> predicate.test(mob, pos, extra) ? pos : null;
    }

    public static <T> IfElseConstructorProcessor<T> testNull(MobPosProcessor<? super T> positiveProcessor) {
        return new IfElseConstructorProcessor<>(positiveProcessor);
    }

    private MobPosProcessors() {}

    @FunctionalInterface
    public interface MobPosFunction<T> {
        MobPosProcessor<T> apply(BlockPos root, Mob mob, RandomSource random, T extra);
    }
}
