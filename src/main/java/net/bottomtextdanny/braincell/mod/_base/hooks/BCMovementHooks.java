package net.bottomtextdanny.braincell.mod._base.hooks;

import net.bottomtextdanny.braincell.base.BCMath;
import net.bottomtextdanny.braincell.mod._base.BCEvaluations;
import net.bottomtextdanny.braincell.mod.capability.BCCapabilityHelper;
import net.bottomtextdanny.braincell.mod.capability.player.BCAccessoryModule;
import net.bottomtextdanny.braincell.mod.capability.player.accessory.extensions.FinnFall;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.Comparator;
import java.util.Objects;

public final class BCMovementHooks {

    public static void livingFallHook(LivingFallEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            playerFallHook(player, event);
        }
    }

    private static void playerFallHook(Player player, LivingFallEvent event) {
        BCAccessoryModule accessoryModule = BCCapabilityHelper.accessoryModule(player);
        MutableFloat damageTransformer = new MutableFloat(event.getDamageMultiplier());
        MutableFloat distanceTransformer = new MutableFloat(event.getDistance());

        accessoryModule.getAllAccessoryList()
                .stream()
                .map(accessory -> accessory instanceof FinnFall module ? module : null)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(FinnFall::fallModificationPriority))
                .forEach(accessory -> {
                    accessory.fallDamageMultModifier(event.getDistance(), damageTransformer, distanceTransformer);
                });

        event.setDamageMultiplier(damageTransformer.floatValue());
        event.setDistance(distanceTransformer.floatValue());
    }

    public static void livingJumpHook(LivingEvent.LivingJumpEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            playerJumpHook(player, event);
        }
    }

    private static void playerJumpHook(Player player, LivingEvent.LivingJumpEvent event) {
        float f = player.getYRot() * BCMath.FRAD;
        float deltaFactor = BCEvaluations.EXTRA_JUMP_FORWARD.test(player) / 10.0F;

        if (deltaFactor > 0.0F) {
            player.setDeltaMovement(player.getDeltaMovement().add(-Mth.sin(f) * deltaFactor, 0.0D, Mth.cos(f) * deltaFactor));
        }
    }
}
