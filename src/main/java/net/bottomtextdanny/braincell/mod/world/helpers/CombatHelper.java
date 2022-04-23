package net.bottomtextdanny.braincell.mod.world.helpers;

import com.google.common.collect.Sets;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;

import java.util.Random;
import java.util.Set;

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

    public static boolean hasValidAttackTarget(Mob mob) {
        return mob.getTarget() != null && mob.getTarget().isAlive() && !mob.isRemoved();
    }

    public static void attackWithMultiplier(Mob attacker, LivingEntity livingEntity, float mult) {
        livingEntity.hurt(DamageSource.mobAttack(attacker), (float)attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) * mult);
    }

    public static float getHealthNormalized(Mob attacker) {
        return attacker.getHealth() / attacker.getMaxHealth();
    }

    public static class EntityCheckHelper {
        public static final Set<Class<? extends LivingEntity>> GOODIES = Sets.newIdentityHashSet();
        public static final Set<Class<? extends LivingEntity>> POST_DRAGON_GOODIES = Sets.newIdentityHashSet();

        public static boolean isGoodie(LivingEntity entity) {
            return GOODIES.contains(entity.getClass());
        }

        public static boolean isPostDragonGoodie(LivingEntity entity) {
            return GOODIES.contains(entity.getClass()) || POST_DRAGON_GOODIES.contains(entity.getClass());
        }

        public static void init() {
            Set<Class<? extends LivingEntity>> set = GOODIES;

            set.add(IronGolem.class);
            set.add(SnowGolem.class);

            set = POST_DRAGON_GOODIES;

            set.add(TraderLlama.class);
            set.add(WanderingTrader.class);
            set.add(Villager.class);
        }
    }
}