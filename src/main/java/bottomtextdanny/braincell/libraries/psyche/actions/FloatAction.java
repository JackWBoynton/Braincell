package bottomtextdanny.braincell.libraries.psyche.actions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FloatAction extends ConstantThoughtAction<PathfinderMob> {
    public static final float DEFAULT_FLOAT_HEIGHT = 0.5F;
    public static final int DETECT_GROUND_REFRESH_RATE = 14;
    private final float floatHeight;
    private boolean cachedDetectedGroundState;

    public FloatAction(PathfinderMob mob, float floatHeight) {
        super(mob, null);
        this.floatHeight = floatHeight;
        mob.getNavigation().setCanFloat(true);
        this.cachedDetectedGroundState = true;
    }

    public FloatAction(PathfinderMob mob) {
        this(mob, DEFAULT_FLOAT_HEIGHT);
    }

    @Override
    protected void update() {
        PathfinderMob mob = this.mob;
        double fluidHeight = mob.getFluidHeight(FluidTags.WATER);

        if (fluidHeight > 0.15D && this.ticksPassed % DETECT_GROUND_REFRESH_RATE == 0) {
            float reducedHeight = mob.getEyeHeight() - 0.15F;
            this.cachedDetectedGroundState = detectGround(reducedHeight, mob);
        }

        if (mob.isInWater() || mob.isInLava()) {
            if (mob.horizontalCollision || !this.cachedDetectedGroundState && fluidHeight > this.floatHeight) {
                if (mob.getRandom().nextFloat() < 0.8F) {
                    mob.getJumpControl().jump();
                }
            }
        }
    }

    public static boolean detectGround(float height, PathfinderMob mob) {
        if (!mob.isInLava() && mob.getFluidHeight(FluidTags.WATER) < height) {
            float gap = (float) (height - mob.getFluidHeight(FluidTags.WATER));
            Vec3 gapVec = mob.position().subtract(0.0D, gap, 0.0D);
            BlockPos gapPos = new BlockPos(gapVec);
            Level level = mob.level;
            double delta = level.getBlockState(gapPos).getCollisionShape(level, gapPos).max(Direction.Axis.Y);

            if (delta <= 0.0D) {
                BlockPos gapPosBelow = gapPos.below();
                double lowerDelta = level.getBlockState(gapPosBelow).getCollisionShape(level, gapPosBelow).max(Direction.Axis.Y);
                delta = lowerDelta - 1.0F;
            }

            return delta > 0.0F && delta + gapPos.getY() - 1.0F < gapVec.y;
        }
        return false;
    }
}
