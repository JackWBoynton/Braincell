package net.bottomtextdanny.braincell.mod.entity.psyche.pos_finder;

import net.bottomtextdanny.braincell.mod._base.entity.psyche.pos_finder.MobPosPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

public final class MobPosPredicates {

    public boolean insideWorldHeight(LevelAccessor level, BlockPos pos) {
        return pos.getY() < level.getMinBuildHeight() || pos.getY() > level.getMaxBuildHeight();
    }

    public static MobPosPredicate unrestricted(PathfinderMob mob) {
        return (entity, bp) -> !mob.isWithinRestriction(bp);
    }

    public static MobPosPredicate noMalus() {
        return (entity, bp) -> entity.getPathfindingMalus(WalkNodeEvaluator.getBlockPathTypeStatic(entity.level, bp.mutable())) <= 0.0F;
    }

    public static MobPosPredicate isWater() {
        return (entity, bp) -> {
            FluidState fluidState = entity.level.getBlockState(bp).getFluidState();

            return fluidState.is(Fluids.WATER) || fluidState.is(Fluids.FLOWING_WATER);
        };
    }

    public static MobPosPredicate isLava() {
        return (entity, bp) -> {
            FluidState fluidState = entity.level.getBlockState(bp).getFluidState();

            return fluidState.is(Fluids.LAVA) || fluidState.is(Fluids.FLOWING_LAVA);
        };
    }

    public static MobPosPredicate noFluid() {
        return (entity, bp) -> {
            return entity.level.getBlockState(bp).getFluidState().is(Fluids.EMPTY);
        };
    }

    public static MobPosPredicate isSolid() {
        return (entity, bp) -> entity.level.getBlockState(bp).getMaterial().isSolid();
    }

    public static MobPosPredicate isStable() {
        return (entity, bp) -> entity.level.getBlockState(bp).isSolidRender(entity.level, bp);
    }

    public static MobPosPredicate is(Block blockState) {
        return (entity, bp) -> entity.level.getBlockState(bp).getBlock() == blockState;
    }

    public static MobPosPredicate has(TagKey<Block> blockState) {
        return (entity, bp) -> entity.level.getBlockState(bp).getBlock().builtInRegistryHolder().is(blockState);
    }

    public static MobPosPredicate blocksMotion() {
        return (entity, bp) -> entity.level.getBlockState(bp).getMaterial().blocksMotion();
    }

    private MobPosPredicates() {}
}
