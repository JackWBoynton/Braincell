package bottomtextdanny.braincell.libraries.psyche.pos_finder;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;

import java.util.Stack;

public final class MobPosStackProcessor<T> implements MobPosProcessor<T> {
    private final Stack<MobPosProcessor<? super T>> processor;

    public MobPosStackProcessor() {
        this.processor = new Stack<>();
    }

    @Override
    public BlockPos compute(BlockPos root, Mob mob, RandomSource random, T extra) {
        BlockPos pos = root;
        for (MobPosProcessor<? super T> t : this.processor) {
            BlockPos iter = t.compute(pos, mob, random, extra);

            if (iter != null) pos = iter;
            else break;
        }
        return pos;
    }

    public MobPosStackProcessor<T> push(MobPosProcessor<? super T> appendable) {
        this.processor.push(appendable);
        return this;
    }

    @Override
    public <U extends T> MobPosStackProcessor<U> generic(Class<U> clazz) {
        return (MobPosStackProcessor<U>) this;
    }
}
