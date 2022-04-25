package net.bottomtextdanny.braincell.mod.entity.psyche.targeting;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.Objects;

public interface TargetPredicate {
    boolean test(Mob Mob, LivingEntity LivingEntity);
    
    default TargetPredicate and(TargetPredicate other) {
        Objects.requireNonNull(other);
        return (Mob Mob, LivingEntity LivingEntity) -> test(Mob, LivingEntity) && other.test(Mob, LivingEntity);
    }
    
    default TargetPredicate negate() {
        return (Mob Mob, LivingEntity LivingEntity) -> !test(Mob, LivingEntity);
    }
    
    default TargetPredicate or(TargetPredicate other) {
        Objects.requireNonNull(other);
        return (Mob Mob, LivingEntity LivingEntity) -> test(Mob, LivingEntity) || other.test(Mob, LivingEntity);
    }
}
