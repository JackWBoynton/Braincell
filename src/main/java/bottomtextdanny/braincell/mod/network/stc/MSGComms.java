package bottomtextdanny.braincell.mod.network.stc;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.mod.network.BCPacket;
import bottomtextdanny.braincell.mod.network.BCPacketInitialization;
import bottomtextdanny.braincell.mod.network.Connection;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public class MSGComms implements BCPacket {
   private final int id;
   private final FriendlyByteBuf dataStream;

   public MSGComms(int id, FriendlyByteBuf stream) {
      this.id = id;
      this.dataStream = stream;
   }

   public void serialize(FriendlyByteBuf stream) {
      stream.writeVarInt(this.id);
      stream.writeBytes(this.dataStream);
   }

   public MSGComms deserialize(FriendlyByteBuf stream) {
      int id = stream.readVarInt();
      return new MSGComms(id, new FriendlyByteBuf(Unpooled.wrappedBuffer(stream.readBytes(stream.readableBytes()))));
   }

   public void postDeserialization(NetworkEvent.Context ctx, Level world) {
      Connection.doClientSide(() -> {
         Braincell.client().setComms(this.id, this.dataStream);
      });
   }

   public LogicalSide side() {
      return LogicalSide.CLIENT;
   }

   public SimpleChannel mainChannel() {
      return BCPacketInitialization.CHANNEL;
   }
}
