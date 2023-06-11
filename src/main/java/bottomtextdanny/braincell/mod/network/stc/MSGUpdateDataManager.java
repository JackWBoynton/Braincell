package bottomtextdanny.braincell.mod.network.stc;

import bottomtextdanny.braincell.mod.entity.modules.data_manager.BCDataManager;
import bottomtextdanny.braincell.mod.entity.modules.data_manager.BCDataManagerProvider;
import bottomtextdanny.braincell.mod.network.BCEntityPacket;
import bottomtextdanny.braincell.mod.network.BCPacketInitialization;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.simple.SimpleChannel;

public final class MSGUpdateDataManager extends BCEntityPacket {
   private final BCDataManager manager;

   public MSGUpdateDataManager(int entityId, BCDataManager manager) {
      super(entityId);
      this.manager = manager;
   }

   public void serialize(FriendlyByteBuf stream) {
      super.serialize(stream);
      this.manager.writePacket(stream);
   }

   public MSGUpdateDataManager deserialize(FriendlyByteBuf stream) {
      MSGUpdateDataManager packet = new MSGUpdateDataManager(stream.readInt(), (BCDataManager)null);
      Entity entity = packet.getEntityAsReceptor(Minecraft.getInstance().player.level);
      if (entity instanceof BCDataManagerProvider dataHolder) {
         dataHolder.bcDataManager().readPacket(stream);
         dataHolder.afterClientDataUpdate();
      }

      return packet;
   }

   public LogicalSide side() {
      return LogicalSide.CLIENT;
   }

   public SimpleChannel mainChannel() {
      return BCPacketInitialization.CHANNEL;
   }
}
