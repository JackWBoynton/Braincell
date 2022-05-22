package bottomtextdanny.braincell.mod.capability.player.accessory.extensions;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import org.apache.commons.lang3.mutable.MutableObject;

public interface FinnHit {
    int H_ADD_OP = 0, H_LAST_MULT_OP = 0;
    int C_ADD_OP = 0, C_ECLIPSE = 1;

    int criticalModificationPriority();

    int hitModificationPriority();

    default void onMeleeAttack(LivingEntity entity, DamageSource source, MutableObject<Float> amount, float baseValue) {}

    default void onMeleeCritical(LivingEntity entity, MutableObject<Float> amount, float baseValue) {}

    default void onIndirectAttack(LivingEntity attacked, IndirectEntityDamageSource source, MutableObject<Float> amount, float baseValue) {
        if (source.getDirectEntity() instanceof Arrow) {
            onArrowAttack(attacked, source, amount, baseValue);
        }
    }

    default void onArrowAttack(LivingEntity attacked, IndirectEntityDamageSource source, MutableObject<Float> amount, float baseValue) {}
}
