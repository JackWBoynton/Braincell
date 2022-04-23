package net.bottomtextdanny.braincell.mod._base.hooks;

import net.bottomtextdanny.braincell.mod.capability.BCCapabilityHelper;
import net.bottomtextdanny.braincell.mod.capability.player.BCAccessoryModule;
import net.bottomtextdanny.braincell.mod.capability.player.accessory.IAccessory;
import net.bottomtextdanny.braincell.mod.capability.player.accessory.extensions.FinnDeath;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.List;

public final class BCDeathHooks {

    public static void livingDeathHook(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            handlePlayerDeath(player);
        }
    }

    private static void handlePlayerDeath(Player player) {
        BCAccessoryModule accessoryModule = BCCapabilityHelper.accessoryModule(player);

        List<IAccessory> accessories = accessoryModule.getAllAccessoryList();

        accessories.forEach((accessory) -> {
            if (accessory instanceof FinnDeath module) {
                module.onDeath();
            }
        });
    }
}
