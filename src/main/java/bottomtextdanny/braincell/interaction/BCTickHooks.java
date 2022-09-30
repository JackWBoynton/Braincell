package bottomtextdanny.braincell.interaction;

import bottomtextdanny.braincell.libraries.capability.BCCapabilityHelper;
import bottomtextdanny.braincell.libraries.accessory.BCAccessoryModule;
import net.minecraftforge.event.TickEvent;

public final class BCTickHooks {

    public static void serverLevelTickHook(TickEvent.LevelTickEvent event) {}

    public static void playerTickHook(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player != null && event.player.isAlive()) {
            BCAccessoryModule bcAccessoryModule = BCCapabilityHelper.accessoryModule(event.player);
            if (bcAccessoryModule != null) {
                bcAccessoryModule.tick();
            }
        }
    }
}
