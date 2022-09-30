package bottomtextdanny.braincell.libraries.psyche.pos_finder;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

public final class MobPosPredicates {

    public <T> boolean insideWorldHeight(LevelAccessor level, BlockPos pos, T extra) {
        return pos.getY() < level.getMinBuildHeight() || pos.getY() > level.getMaxBuildHeight();
    }

    public static <T> MobPosPredicate<T> unrestricted(PathfinderMob mob) {
        return (entity, bp, extra) -> !mob.isWithinRestriction(bp);
    }

    public static <T> MobPosPredicate<T> noMalus() {
        return (entity, bp, extra) -> entity.getPathfindingMalus(WalkNodeEvaluator.getBlockPathTypeStatic(entity.level, bp.mutable())) <= 0.0F;
    }

    public static <T> MobPosPredicate<T> isWater() {
        return (entity, bp, extra) -> {
            FluidState fluidState = entity.level.getBlockState(bp).getFluidState();

            return fluidState.is(Fluids.WATER) || fluidState.is(Fluids.FLOWING_WATER);
        };
    }

    public static <T> MobPosPredicate<T> isLava() {
        return (entity, bp, extra) -> {
            FluidState fluidState = entity.level.getBlockState(bp).getFluidState();

            return fluidState.is(Fluids.LAVA) || fluidState.is(Fluids.FLOWING_LAVA);
        };
    }

    public static <T> MobPosPredicate<T> noFluid() {
        return (entity, bp, extra) -> {
            return entity.level.getBlockState(bp).getFluidState().is(Fluids.EMPTY);
        };
    }

    public static <T> MobPosPredicate<T> isSolid() {
        return (entity, bp, extra) -> entity.level.getBlockState(bp).getMaterial().isSolid();
    }

    public static <T> MobPosPredicate<T> isStable() {
        return (entity, bp, extra) -> entity.level.getBlockState(bp).isSolidRender(entity.level, bp);
    }

    public static <T> MobPosPredicate<T> is(Block blockState) {
        return (entity, bp, extra) -> entity.level.getBlockState(bp).getBlock() == blockState;
    }

    public static <T> MobPosPredicate<T> has(TagKey<Block> blockState) {
        return (entity, bp, extra) -> entity.level.getBlockState(bp).getBlock().builtInRegistryHolder().is(blockState);
    }

    public static <T> MobPosPredicate<T> blocksMotion() {
        return (entity, bp, extra) -> entity.level.getBlockState(bp).getMaterial().blocksMotion();
    }

    private MobPosPredicates() {}
}
