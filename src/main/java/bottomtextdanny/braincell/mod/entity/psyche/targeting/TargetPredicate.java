package bottomtextdanny.braincell.mod.entity.psyche.targeting;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.Objects;

@Deprecated
public interface TargetPredicate extends MobMatchPredicate<LivingEntity> {
    boolean test(Mob Mob, LivingEntity LivingEntity);

    @Override
    default TargetPredicate and(MobMatchPredicate<? super LivingEntity> other) {
        Objects.requireNonNull(other);
        return (Mob Mob, LivingEntity LivingEntity) -> test(Mob, LivingEntity) && other.test(Mob, LivingEntity);
    }

    @Override
    default TargetPredicate negate() {
        return (Mob Mob, LivingEntity LivingEntity) -> !test(Mob, LivingEntity);
    }

    @Override
    default TargetPredicate or(MobMatchPredicate<? super LivingEntity> other) {
        Objects.requireNonNull(other);
        return (Mob Mob, LivingEntity LivingEntity) -> test(Mob, LivingEntity) || other.test(Mob, LivingEntity);
    }
}
