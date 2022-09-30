package bottomtextdanny.braincell.libraries.psyche.pos_finder;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;

import javax.annotation.Nullable;

public final class IfElseConstructorProcessor<T> implements MobPosProcessor<T> {
    private final MobPosProcessor<? super T> positiveProcessor;
    @Nullable
    private MobPosProcessor<? super T> negativeProcessor;

    public IfElseConstructorProcessor(MobPosProcessor<? super T> positiveProcessor) {
        this.positiveProcessor = positiveProcessor;
    }

    @Nullable
    @Override
    public BlockPos compute(BlockPos root, Mob mob, RandomSource random, T extra) {
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
