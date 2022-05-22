package bottomtextdanny.braincell.mod.world.helpers;

import bottomtextdanny.braincell.mod.entity.psyche.input.ActionInputKey;
import bottomtextdanny.braincell.mod.entity.psyche.targeting.MobMatchPredicate;
import bottomtextdanny.braincell.mod.world.entity_utilities.PsycheEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.Random;

public final class CombatHelper {
    private static final Random RANDOM = new Random();

    public static void mayDisableShield(Entity target, int ticks, float possibility) {
        if (target instanceof Player player && RANDOM.nextFloat() < possibility) {
            if (player.isBlocking()) {
                if (!player.getCooldowns().isOnCooldown(player.getUseItem().getItem())) {
                    player.level.broadcastEntityEvent(target, (byte)30);
                }
                player.getCooldowns().addCooldown(player.getUseItem().getItem(), ticks);
                player.stopUsingItem();
            }
        }
    }

    public static void disableShield(Entity target, int ticks) {
        mayDisableShield(target, ticks, 1.0F);
    }

    public static void attackWithMultiplier(Mob attacker, LivingEntity livingEntity, float mult) {
        livingEntity.hurt(DamageSource.mobAttack(attacker), (float)attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) * mult);
    }

    public static float getHealthNormalized(Mob attacker) {
        return attacker.getHealth() / attacker.getMaxHealth();
    }

    public static boolean hasLivingAttackTarget(Mob mob) {
        return mob.getTarget() != null && mob.getTarget().isAlive() && !mob.isRemoved();
    }

    public static boolean isValidAttackTarget(LivingEntity mob) {
        return mob != null && mob.isAlive()
                && !mob.isRemoved() && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(mob);
    }

    public static boolean isValidAttackTarget(Mob targeter, LivingEntity target) {
        if (targeter instanceof PsycheEntity psyched && psyched.getPsyche() != null) {
            MobMatchPredicate<LivingEntity> pred =  psyched.getPsyche().getInputs().getOfDefault(ActionInputKey.TARGET_VALIDATOR);

            return target != null && target.isAlive()
                    && !target.isRemoved() && pred.test(targeter, target);
        }

        return target != null && target.isAlive()
                && !target.isRemoved() && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target);
    }
}
