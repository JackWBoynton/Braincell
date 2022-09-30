package bottomtextdanny.braincell.libraries.accessory;

import bottomtextdanny.braincell.libraries.capability.CapabilityModule;
import bottomtextdanny.braincell.libraries.capability.CapabilityWrap;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class BCAccessoryCapability extends CapabilityWrap<BCAccessoryCapability, Player> {
    public static Capability<BCAccessoryCapability> TOKEN = CapabilityManager.get(new CapabilityToken<>(){});
    private final BCAccessoryModule accessories;

    public BCAccessoryCapability(Player holder) {
        super(holder);
        this.accessories = new BCAccessoryModule(this);
    }

    public BCAccessoryModule getAccessories() {
        return accessories;
    }

    @Override
    protected Capability<?> getToken() {
        return TOKEN;
    }
}
