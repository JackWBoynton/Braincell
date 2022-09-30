package bottomtextdanny.braincell.interaction;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.network.Connection;
import bottomtextdanny.braincell.libraries.speck.BCSpeckCapability;
import bottomtextdanny.braincell.libraries.accessory.BCAccessoryCapability;
import bottomtextdanny.braincell.libraries.capability.BCCapabilityProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

public class BCCapabilityHooks {

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(BCSpeckCapability.class);
        event.register(BCAccessoryCapability.class);
    }

    public static void attachToLevel(AttachCapabilitiesEvent<Level> event) {
        Connection.doClientSide(() -> {
            addCap(event, "speck", new BCCapabilityProvider<>(BCSpeckCapability.TOKEN, LazyOptional.of(() -> new BCSpeckCapability(event.getObject()))));
        });
    }

    public static void attachToEntity(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            addCap(event, "acessories", new BCCapabilityProvider<>(BCAccessoryCapability.TOKEN, LazyOptional.of(() -> new BCAccessoryCapability(player))));
        }
    }

    private static void addCap(AttachCapabilitiesEvent<?> event, String key, ICapabilityProvider cap) {
        event.addCapability(new ResourceLocation(Braincell.ID, key), cap);
    }
}
