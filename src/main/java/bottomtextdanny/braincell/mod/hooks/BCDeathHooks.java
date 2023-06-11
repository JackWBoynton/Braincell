package bottomtextdanny.braincell.mod.hooks;

import bottomtextdanny.braincell.mod.capability.BCCapabilityHelper;
import bottomtextdanny.braincell.mod.capability.player.BCAccessoryModule;
import bottomtextdanny.braincell.mod.capability.player.accessory.extensions.FinnDeath;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public final class BCDeathHooks {
   public static void livingDeathHook(LivingDeathEvent event) {
      LivingEntity var2 = event.getEntityLiving();
      if (var2 instanceof Player player) {
         handlePlayerDeath(player);
      }

   }

   private static void handlePlayerDeath(Player player) {
      BCAccessoryModule accessoryModule = BCCapabilityHelper.accessoryModule(player);
      List accessories = accessoryModule.getAllAccessoryList();
      accessories.forEach((accessory) -> {
         if (accessory instanceof FinnDeath module) {
            module.onDeath();
         }

      });
   }
}
