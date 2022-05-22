package bottomtextdanny.braincell.mod.capability;

import bottomtextdanny.braincell.mod.capability.level.BCLevelCapability;
import bottomtextdanny.braincell.mod.capability.player.BCPlayerCapability;
import bottomtextdanny.braincell.Braincell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Braincell.ID)
public class BCCapabilityManager {

    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event) {
        event.register(BCLevelCapability.class);
        event.register(BCPlayerCapability.class);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void attachToLevel(AttachCapabilitiesEvent<Level> event) {
        addCap(event, "level_cap", new BCLevelCapability(event.getObject()));
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void attachToEntity(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            addCap(event, "player_cap", new BCPlayerCapability(player));
        }
    }

    static void addCap(AttachCapabilitiesEvent<?> event, String key, ICapabilityProvider cap) {
        event.addCapability(new ResourceLocation(Braincell.ID, key), cap);
    }
}
