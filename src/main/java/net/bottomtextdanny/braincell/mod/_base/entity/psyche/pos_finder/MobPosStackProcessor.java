package net.bottomtextdanny.braincell.mod._base.entity.psyche.pos_finder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;

import java.util.Stack;
import java.util.random.RandomGenerator;

public final class MobPosStackProcessor implements MobPosProcessor {
    private final Stack<MobPosProcessor> processor;

    public MobPosStackProcessor() {
        this.processor = new Stack<>();
    }

    @Override
    public BlockPos compute(BlockPos root, Mob mob, RandomGenerator random) {
        BlockPos pos = root;
        for (MobPosProcessor t : this.processor) {
            BlockPos iter = t.compute(pos, mob, random);

            if (iter != null) pos = iter;
            else break;
        }
        return pos;
    }

    public MobPosStackProcessor push(MobPosProcessor appendable) {
        this.processor.push(appendable);
        return this;
    }
}
