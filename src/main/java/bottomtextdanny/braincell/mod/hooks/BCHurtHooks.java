package bottomtextdanny.braincell.mod.hooks;

import bottomtextdanny.braincell.mod.capability.BCCapabilityHelper;
import bottomtextdanny.braincell.mod.capability.player.BCAccessoryModule;
import bottomtextdanny.braincell.mod.capability.player.accessory.extensions.FinnHurt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.apache.commons.lang3.mutable.MutableObject;

public class BCHurtHooks {
   public static void livingHurtHook(LivingHurtEvent event) {
      Entity var2 = event.getEntity();
      if (var2 instanceof Player player) {
         handlePlayerHurt(player, event);
      }

   }

   private static void handlePlayerHurt(Player player, LivingHurtEvent event) {
      BCAccessoryModule accessoryModule = BCCapabilityHelper.accessoryModule(player);
      MutableObject damageTransformer = new MutableObject(event.getAmount());
      accessoryModule.getAllAccessoryList().forEach((acc) -> {
         if (acc instanceof FinnHurt hurtModule) {
            hurtModule.onHurt(event.getSource(), damageTransformer);
         }

      });
      event.setAmount((Float)damageTransformer.getValue());
   }
}
