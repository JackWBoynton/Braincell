package net.bottomtextdanny.braincell.mod.entity.psyche.pos_finder;

import net.bottomtextdanny.braincell.base.BCMath;
import net.bottomtextdanny.braincell.base.function.TriFunction;
import net.bottomtextdanny.braincell.base.value_mapper.FloatMapper;
import net.bottomtextdanny.braincell.mod._base.entity.psyche.pos_finder.IfElseConstructorProcessor;
import net.bottomtextdanny.braincell.mod._base.entity.psyche.pos_finder.MobPosPredicate;
import net.bottomtextdanny.braincell.mod._base.entity.psyche.pos_finder.MobPosProcessor;
import net.bottomtextdanny.braincell.mod._base.entity.psyche.pos_finder.MobPosStackProcessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.random.RandomGenerator;

public final class MobPosProcessors {

    public static MobPosProcessor compose(TriFunction<BlockPos, Mob, RandomGenerator, MobPosProcessor> processor) {
        return (pos, mob, r) -> processor.apply(pos, mob, r).compute(pos, mob, r);
    }

    public static MobPosStackProcessor stack() {
        return new MobPosStackProcessor();
    }

    public static MobPosProcessor randomOffset(int xAxis, int yAxis, int zAxis) {
        return (pos, mob, r) -> pos.offset(
                r.nextInt(-xAxis, xAxis),
                r.nextInt(-yAxis, yAxis),
                r.nextInt(-zAxis, zAxis));
    }

    public static MobPosProcessor offset(int x, int y, int z) {
        return (pos, mob, r) -> pos.offset(x, y, z);
    }

    public static MobPosProcessor advanceHorizontal(FloatMapper radianAngleMapper,
                                                    FloatMapper stepMapper) {
        return (pos, mob, r) -> {
            float radianAngle = radianAngleMapper.map(r);
            float step = stepMapper.map(r);

            return pos.offset(
                    BCMath.sin(radianAngle) * step,
                    0,
                    BCMath.cos(radianAngle) * step);
        };
    }

    public static MobPosProcessor advance(FloatMapper radianVerticalAngleMapper,
                                          FloatMapper radianHorizontalAngleMapper,
                                          FloatMapper stepMapper) {
        return (pos, mob, r) -> {
            Vec3 vec = Vec3.directionFromRotation(
                    radianVerticalAngleMapper.map(r),
                    radianHorizontalAngleMapper.map(r))
                    .scale(stepMapper.map(r));

            return pos.offset(vec.x, vec.y, vec.z);
        };
    }

    public static MobPosProcessor find(int tries, MobPosProcessor processor) {
        return (pos, mob, r) -> {
            for (int i = 0; i < tries; i++) {
                BlockPos att = processor.compute(pos, mob, r);
                if (att != null) return att;
            }

            return null;
        };
    }

    public static MobPosProcessor sample(int tries, MobPosProcessor processor, Comparator<BlockPos> posComparator) {
        return (pos, mob, r) -> {
            BlockPos newPos = null;
            for (int i = 0; i < tries; i++) {
                BlockPos att = processor.compute(pos, mob, r);
                if (newPos == null || (att != null && posComparator.compare(newPos, att) > 0)) {
                    newPos = att;
                }
            }

            return newPos;
        };
    }

    public static MobPosProcessor moveIf(Direction direction, int max, MobPosPredicate predicate) {
        return (pos, mob, r) -> {
            BlockPos.MutableBlockPos mu = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());

            for (int i = 0; i < max && predicate.test(mob, mu); i++) {
                mu.move(direction);
            }

            return new BlockPos(mu);
        };
    }

    public static MobPosProcessor move(Direction direction, FloatMapper distance) {
        return (pos, mob, r) -> {
            return pos.relative(direction, (int)distance.map(r));
        };
    }

    public static MobPosProcessor pred(MobPosPredicate predicate) {
        return (pos, mob, r) -> predicate.test(mob, pos) ? pos : null;
    }

    public static IfElseConstructorProcessor testNull(MobPosProcessor positiveProcessor) {
        return new IfElseConstructorProcessor(positiveProcessor);
    }

    private MobPosProcessors() {}
}
