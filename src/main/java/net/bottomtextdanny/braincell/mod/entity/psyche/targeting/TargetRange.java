package net.bottomtextdanny.braincell.mod.entity.psyche.targeting;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;

public abstract class TargetRange implements TargetPredicate {

    private TargetRange() {
        super();
    }

    public static TargetRange followRangeMultiplied(float factor) {
        return new ByFollowRange(factor);
    }

    public static TargetRange followRange() {
        return new ByFollowRange(1.0F);
    }

    public static TargetRange fixedRange(double distance) {
        return new Fixed(distance);
    }

    private static class ByFollowRange extends TargetRange {
        private final float multiplier;

        public ByFollowRange(float multiplier) {
            super();
            this.multiplier = multiplier;
        }

        @Override
        public boolean test(Mob opinionated, LivingEntity entity) {
            return opinionated.distanceTo(entity) < opinionated.getAttribute(Attributes.FOLLOW_RANGE).getValue() * this.multiplier;
        }
    }

    private static class Fixed extends TargetRange {
        private final double distance;

        public Fixed(double distance) {
            super();
            this.distance = distance;
        }

        @Override
        public boolean test(Mob opinionated, LivingEntity entity) {
            return opinionated.distanceTo(entity) < this.distance;
        }
    }
}