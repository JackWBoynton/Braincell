package bottomtextdanny.braincell.interaction;

import bottomtextdanny.braincell.libraries.capability.BCCapabilityHelper;
import bottomtextdanny.braincell.libraries.accessory.BCAccessoryModule;
import bottomtextdanny.braincell.libraries.accessory.IAccessory;
import bottomtextdanny.braincell.libraries.accessory.extensions.FinnDeath;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.List;

public final class BCDeathHooks {

    public static void livingDeathHook(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
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
