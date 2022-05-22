package bottomtextdanny.braincell.mod.entity.psyche.targeting;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import javax.annotation.Nullable;

public class Targeter implements TargetPredicate {
    private final TargetRange targetRange;
    @Nullable
    private final TargetRange targetRangeForInvisible;
    private final boolean forCombat;

    private Targeter(TargetRange targetRange, @Nullable TargetRange targetRangeForInvisible, boolean beOnSight, boolean forCombat) {
        this.targetRange = targetRange;
        this.targetRangeForInvisible = targetRangeForInvisible;
        this.forCombat = forCombat;
    }

    public boolean test(Mob selector, LivingEntity posibleTarget) {
        if (selector == null || selector == posibleTarget) {
            return false;
        } else if (!posibleTarget.canBeSeenByAnyone()) {
            return false;
        } else {
            if (this.forCombat && (!selector.canAttack(posibleTarget) || !selector.canAttackType(posibleTarget.getType()) || selector.isAlliedTo(posibleTarget))) {
                return false;
            }
            if (posibleTarget.isInvisible()) {
                return this.targetRangeForInvisible != null && this.targetRangeForInvisible.test(selector, posibleTarget);
            } else return this.targetRange.test(selector, posibleTarget);
        }
    }

    public static class Builder {
        private final TargetRange targetRange;
        @Nullable
        private TargetRange targetRangeForInvisible;
        private boolean hasToBeOnSight;
        private boolean forCombat;

        private Builder(TargetRange targetRange) {
            this.targetRange = targetRange;
        }

        public static <T extends LivingEntity> Targeter.Builder start(TargetRange targetRange) {
            return new Targeter.Builder(targetRange);
        }

        public Targeter.Builder targetRangeForInvisible(TargetRange targetRangeForInvisible) {
            this.targetRangeForInvisible = targetRangeForInvisible;
            return this;
        }

        public Targeter.Builder hasToBeOnSight() {
            this.hasToBeOnSight = true;
            return this;
        }

        public Targeter.Builder isForCombat() {
            this.forCombat = true;
            return this;
        }

        public Targeter build() {
            return new Targeter(this.targetRange, this.targetRangeForInvisible, this.hasToBeOnSight, this.forCombat);
        }
    }
}
