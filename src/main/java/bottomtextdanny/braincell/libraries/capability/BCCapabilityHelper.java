package bottomtextdanny.braincell.libraries.capability;

import bottomtextdanny.braincell.libraries.accessory.BCAccessoryCapability;
import bottomtextdanny.braincell.libraries.accessory.BCAccessoryModule;
import net.minecraft.world.entity.player.Player;

public final class BCCapabilityHelper {

    public static BCAccessoryModule accessoryModule(Player player) {
        BCAccessoryCapability bcPlayerCapability = player.getCapability(BCAccessoryCapability.TOKEN)
                .orElseThrow(() -> new IllegalStateException("Couldn't fetch accessory module from player."));
        return bcPlayerCapability.getAccessories();
    }
}
