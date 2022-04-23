package net.bottomtextdanny.braincell.mod._base.plotter.iterator_support;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;

public final class PlotterPredicates {

    public static Predicate<BlockState> bsHasTag(TagKey<Block> tag) {
        return (bs) -> bs.getBlock().builtInRegistryHolder().is(tag);
    }

    public static Predicate<BlockState> bsIs(Block block) {
        return (bs) -> bs.getBlock() == block;
    }

    public static Predicate<BlockPos> bpBlockState(LevelAccessor level, Predicate<BlockState> predicate) {
        return (bp) -> predicate.test(level.getBlockState(bp));
    }
}
