package bottomtextdanny.braincell.mod.capability;

import bottomtextdanny.braincell.mod.capability.level.BCLevelCapability;
import bottomtextdanny.braincell.mod.capability.player.BCPlayerCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(
   modid = "braincell"
)
public class BCCapabilityManager {
   @SubscribeEvent
   public static void register(RegisterCapabilitiesEvent event) {
      event.register(BCLevelCapability.class);
      event.register(BCPlayerCapability.class);
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public static void attachToLevel(AttachCapabilitiesEvent event) {
      addCap(event, "level_cap", new BCLevelCapability((Level)event.getObject()));
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public static void attachToEntity(AttachCapabilitiesEvent event) {
      Object var2 = event.getObject();
      if (var2 instanceof Player player) {
         addCap(event, "player_cap", new BCPlayerCapability(player));
      }

   }

   static void addCap(AttachCapabilitiesEvent event, String key, ICapabilityProvider cap) {
      event.addCapability(new ResourceLocation("braincell", key), cap);
   }
}
