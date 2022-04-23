package net.bottomtextdanny.braincell.mod._base.entity.psyche.pos_finder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;

import javax.annotation.Nullable;
import java.util.random.RandomGenerator;

public final class IfElseConstructorProcessor implements MobPosProcessor {
    private final MobPosProcessor positiveProcessor;
    @Nullable
    private MobPosProcessor negativeProcessor;

    public IfElseConstructorProcessor(MobPosProcessor positiveProcessor) {
        this.positiveProcessor = positiveProcessor;
    }

    @Nullable
    @Override
    public BlockPos compute(BlockPos root, Mob mob, RandomGenerator random) {
        BlockPos pos = positiveProcessor.compute(root, mob, random);
        return pos == null ?
                negativeProcessor == null ?
                        null :
                        negativeProcessor.compute(root, mob, random) :
                pos;
    }

    public MobPosProcessor fall(MobPosProcessor negativeProcessor) {
        this.negativeProcessor = negativeProcessor;
        return this;
    }
}
