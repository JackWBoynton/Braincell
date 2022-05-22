package bottomtextdanny.braincell.mod.hooks;

import bottomtextdanny.braincell.mod.entity.modules.data_manager.BCDataManagerProvider;
import bottomtextdanny.braincell.mod.network.stc.MSGUpdateDataManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.network.PacketDistributor;

public final class BCConnectionHooks {

    public static void trackEntityHook(PlayerEvent.StartTracking event){
        if (!event.getTarget().level.isClientSide()) {
            if (event.getTarget() instanceof BCDataManagerProvider dataHolder) {
                new MSGUpdateDataManager(event.getTarget().getId(), dataHolder.bcDataManager()).sendTo(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getPlayer()));
            }
        }
    }
}
