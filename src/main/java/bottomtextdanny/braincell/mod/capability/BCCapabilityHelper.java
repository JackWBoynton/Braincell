package bottomtextdanny.braincell.mod.capability;

import bottomtextdanny.braincell.mod.capability.player.BCAccessoryModule;
import bottomtextdanny.braincell.mod.capability.player.BCPlayerCapability;
import net.minecraft.world.entity.player.Player;

public final class BCCapabilityHelper {

    public static BCAccessoryModule accessoryModule(Player player) {
        return player.getCapability(BCPlayerCapability.TOKEN).orElse(null).getAccessories();
    }
}
