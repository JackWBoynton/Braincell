package net.bottomtextdanny.braincell.mod.hooks;

import net.bottomtextdanny.braincell.mod.capability.BCCapabilityHelper;
import net.minecraftforge.event.TickEvent;

public final class BCTickHooks {

    public static void serverLevelTickHook(TickEvent.WorldTickEvent event) {}

    public static void playerTickHook(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player != null && event.player.isAlive()) {
            BCCapabilityHelper.accessoryModule(event.player).tick();
        }
    }
}
