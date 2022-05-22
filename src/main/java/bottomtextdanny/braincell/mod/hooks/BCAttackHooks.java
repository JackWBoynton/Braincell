package bottomtextdanny.braincell.mod.hooks;

import bottomtextdanny.braincell.mod.capability.BCCapabilityHelper;
import bottomtextdanny.braincell.mod.capability.player.BCAccessoryModule;
import bottomtextdanny.braincell.mod.capability.player.accessory.extensions.CriticalStateMutable;
import bottomtextdanny.braincell.mod.capability.player.accessory.extensions.FinnHit;
import bottomtextdanny.braincell.mod.capability.player.accessory.extensions.FinnMakeMeleeCritical;
import bottomtextdanny.braincell.mod.capability.player.accessory.extensions.ForceCriticalDirectValue;
import bottomtextdanny.braincell.mod.world.entity_utilities.EntityHurtCallout;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.Comparator;
import java.util.Objects;

public final class BCAttackHooks {

    public static void damageLivingHook(LivingDamageEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            playerDamageHook(player, event);
        }

        if (event.getEntityLiving() instanceof EntityHurtCallout) {
            event.setAmount(((EntityHurtCallout) event.getEntityLiving()).hurtCallOut(event.getAmount(), event.getSource()));
        }
    }

    public static void playerDamageHook(Player player, LivingDamageEvent event) {
        if (player.isAlive()) {
            BCAccessoryModule accessoryModule = BCCapabilityHelper.accessoryModule(player);
            MutableObject<Float> damageTransformer = new MutableObject<>(event.getAmount());
            if (event.getSource() instanceof IndirectEntityDamageSource source) {
                accessoryModule.getAllAccessoryList()
                        .stream()
                        .map(accessory -> accessory instanceof FinnHit module ? module : null)
                        .filter(Objects::nonNull)
                        .sorted(Comparator.comparingInt(FinnHit::hitModificationPriority))
                        .forEach(accessory -> {
                            accessory.onIndirectAttack(event.getEntityLiving(), source, damageTransformer, event.getAmount());
                        });
                event.setAmount(damageTransformer.getValue());
            } else {
                accessoryModule.getAllAccessoryList()
                        .stream()
                        .map(accessory -> accessory instanceof FinnHit module ? module : null)
                        .filter(Objects::nonNull)
                        .sorted(Comparator.comparingInt(FinnHit::hitModificationPriority))
                        .forEach(accessory -> {
                            accessory.onMeleeAttack(event.getEntityLiving(), event.getSource(), damageTransformer, event.getAmount());
                        });
                event.setAmount(damageTransformer.getValue());
            }
        }
    }

    public static void criticalHitHook(CriticalHitEvent event) {
        Player player = event.getPlayer();
        if (player.isAlive() && event.getTarget() instanceof LivingEntity livingTarget) {
            BCAccessoryModule accessoryModule = BCCapabilityHelper.accessoryModule(player);
            MutableFloat chanceTransformer = new MutableFloat(0.0F);
            CriticalStateMutable directState = new CriticalStateMutable();

            accessoryModule.getAllAccessoryList()
                    .stream()
                    .map(accessory -> accessory instanceof FinnMakeMeleeCritical module ? module : null)
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparingInt(FinnMakeMeleeCritical::critMakingModulePriority))
                    .forEach(accessory -> {
                        accessory.makeMeleeCritical(livingTarget, chanceTransformer, directState);
                    });

            if (directState.getState() != ForceCriticalDirectValue.STRICTLY_NOT_HAPPEN) {
                if (directState.getState() == ForceCriticalDirectValue.HAPPEN || directState.getState() == ForceCriticalDirectValue.STRICTLY_HAPPEN || player.getRandom().nextFloat() < chanceTransformer.getValue()) {
                    event.setResult(Event.Result.ALLOW);
                }
            }

            if (event.getResult() == Event.Result.ALLOW || (event.isVanillaCritical() && event.getResult() == Event.Result.DEFAULT)) {
                MutableObject<Float> damageTransformer = new MutableObject<>(event.getDamageModifier());
                accessoryModule.getAllAccessoryList()
                        .stream()
                        .map(accessory -> accessory instanceof FinnHit module ? module : null)
                        .filter(Objects::nonNull)
                        .sorted(Comparator.comparingInt(FinnHit::criticalModificationPriority))
                        .forEach(accessory -> {
                            accessory.onMeleeCritical(livingTarget, damageTransformer, event.getDamageModifier());
                        });
                event.setDamageModifier(damageTransformer.getValue());
            }
        }
    }
}
