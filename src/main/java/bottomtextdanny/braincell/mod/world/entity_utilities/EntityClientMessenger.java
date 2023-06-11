package bottomtextdanny.braincell.mod.world.entity_utilities;

import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.mod._base.serialization.WorldPacketData;
import bottomtextdanny.braincell.mod.network.stc.MSGEntityClientMessenger;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;

public interface EntityClientMessenger {
   @OnlyIn(Dist.CLIENT)
   default void clientCallOutHandler(int flag, ObjectFetcher fetcher) {
   }

   default void sendClientMsg(int flag, @Nullable WorldPacketData... o) {
      this.sendClientMsg(flag, PacketDistributor.TRACKING_ENTITY.with(() -> {
         return (Entity)this;
      }), o);
   }

   default void sendClientMsg(int flag, PacketDistributor.PacketTarget distributor, @Nullable WorldPacketData... o) {
      Entity asEntity = (Entity)this;
      Level var6 = asEntity.level;
      if (var6 instanceof ServerLevel level) {
         (new MSGEntityClientMessenger(((Entity)this).getId(), flag, level, o)).sendTo(distributor);
      }

   }

   default void sendClientMsg(int flag) {
      this.sendClientMsg(flag, (WorldPacketData)null);
   }
}
