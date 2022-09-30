package bottomtextdanny.braincell.interaction;

import bottomtextdanny.braincell.libraries.capability.BCCapabilityHelper;
import bottomtextdanny.braincell.libraries.accessory.BCAccessoryModule;
import bottomtextdanny.braincell.libraries.accessory.extensions.FinnHurt;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.apache.commons.lang3.mutable.MutableObject;

public class BCHurtHooks {

    public static void livingHurtHook(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            handlePlayerHurt(player, event);
        }
    }

    private static void handlePlayerHurt(Player player, LivingHurtEvent event) {
        BCAccessoryModule accessoryModule = BCCapabilityHelper.accessoryModule(player);

        if (accessoryModule == null) return;

        MutableObject<Float> damageTransformer = new MutableObject<>(event.getAmount());
        accessoryModule.getAllAccessoryList().forEach(acc -> {
            if (acc instanceof FinnHurt hurtModule) {
                hurtModule.onHurt(event.getSource(), damageTransformer);
            }
        });
        event.setAmount(damageTransformer.getValue());
    }
}
