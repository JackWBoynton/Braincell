package bottomtextdanny.braincell.mod.hooks;

import bottomtextdanny.braincell.mod.entity.modules.data_manager.BCDataManagerProvider;
import bottomtextdanny.braincell.mod.network.stc.MSGUpdateDataManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.network.PacketDistributor;

public final class BCConnectionHooks {
   public static void trackEntityHook(PlayerEvent.StartTracking event) {
      if (!event.getTarget().level.isClientSide()) {
         Entity var2 = event.getTarget();
         if (var2 instanceof BCDataManagerProvider) {
            BCDataManagerProvider dataHolder = (BCDataManagerProvider)var2;
            (new MSGUpdateDataManager(event.getTarget().getId(), dataHolder.bcDataManager())).sendTo(PacketDistributor.PLAYER.with(() -> {
               return (ServerPlayer)event.getPlayer();
            }));
         }
      }

   }
}
