package bottomtextdanny.braincell.mod.hooks;

import bottomtextdanny.braincell.mod.capability.BCCapabilityHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;

public final class BCTickHooks {
   public static void serverLevelTickHook(TickEvent.WorldTickEvent event) {
   }

   public static void playerTickHook(TickEvent.PlayerTickEvent event) {
      if (event.phase == Phase.END && event.player != null && event.player.isAlive()) {
         BCCapabilityHelper.accessoryModule(event.player).tick();
      }

   }
}
