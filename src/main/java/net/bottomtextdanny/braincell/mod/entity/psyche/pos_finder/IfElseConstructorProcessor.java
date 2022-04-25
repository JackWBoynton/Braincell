package net.bottomtextdanny.braincell.mod.entity.psyche.pos_finder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;

import javax.annotation.Nullable;
import java.util.random.RandomGenerator;

public final class IfElseConstructorProcessor<T> implements MobPosProcessor<T> {
    private final MobPosProcessor<? super T> positiveProcessor;
    @Nullable
    private MobPosProcessor<? super T> negativeProcessor;

    public IfElseConstructorProcessor(MobPosProcessor<? super T> positiveProcessor) {
        this.positiveProcessor = positiveProcessor;
    }

    @Nullable
    @Override
    public BlockPos compute(BlockPos root, Mob mob, RandomGenerator random, T extra) {
        BlockPos pos = positiveProcessor.compute(root, mob, random, extra);
        return pos == null ?
                negativeProcessor == null ?
                        null :
                        negativeProcessor.compute(root, mob, random, extra) :
                pos;
    }

    public MobPosProcessor<T> fall(MobPosProcessor<? super T> negativeProcessor) {
        this.negativeProcessor = negativeProcessor;
        return this;
    }

    @Override
    public <U extends T> IfElseConstructorProcessor<U> generic(Class<U> clazz) {
        return (IfElseConstructorProcessor<U>) this;
    }
}
