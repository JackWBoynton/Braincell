package bottomtextdanny.braincell.mod.network.stc;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.mod._base.serialization.SerializerMark;
import bottomtextdanny.braincell.mod._base.serialization.WorldPacketData;
import bottomtextdanny.braincell.mod._base.serialization.util.H_WorldDataParser;
import bottomtextdanny.braincell.mod.network.BCEntityPacket;
import bottomtextdanny.braincell.mod.network.BCPacketInitialization;
import bottomtextdanny.braincell.mod.network.Connection;
import bottomtextdanny.braincell.mod.world.entity_utilities.EntityClientMessenger;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public final class MSGEntityClientMessenger extends BCEntityPacket {
   private final int flag;
   private WorldPacketData[] serverObjects;
   private ServerLevel serverLevel;
   private Object[] clientObjects;

   @OnlyIn(Dist.CLIENT)
   public MSGEntityClientMessenger(FriendlyByteBuf stream) {
      super(stream.readInt());
      this.flag = stream.readInt();
      int size = stream.readInt();
      this.clientObjects = new Object[size];
      Connection.doClientSide(() -> {
         if (size > 0) {
            for(int i = 0; i < size; ++i) {
               int serializerId = stream.readInt();
               if (serializerId != -1) {
                  SerializerMark serializer = Braincell.common().getSerializerLookUp().getSerializerFromId(serializerId);
                  if (serializer != null) {
                     this.clientObjects[i] = H_WorldDataParser.readDataFromPacket(stream, serializer, Minecraft.getInstance().level);
                  }
               }
            }
         }

      });
   }

   public MSGEntityClientMessenger(int entityId, int flag, ServerLevel level, @Nullable WorldPacketData[] objects) {
      super(entityId);
      this.flag = flag;
      this.serverObjects = objects;
      this.serverLevel = level;
   }

   public void serialize(FriendlyByteBuf stream) {
      super.serialize(stream);
      stream.writeInt(this.flag);
      if (this.serverObjects == null) {
         stream.writeInt(0);
      } else {
         stream.writeInt(this.serverObjects.length);
         WorldPacketData[] var2 = this.serverObjects;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            WorldPacketData object = var2[var4];
            if (object != null) {
               stream.writeInt(Braincell.common().getSerializerLookUp().getIdFromSerializer(object.getSerializer()));
               object.writeToStream(stream, this.serverLevel);
            } else {
               stream.writeInt(-1);
            }
         }
      }

   }

   @OnlyIn(Dist.CLIENT)
   public MSGEntityClientMessenger deserialize(FriendlyByteBuf stream) {
      return new MSGEntityClientMessenger(stream);
   }

   @OnlyIn(Dist.CLIENT)
   public void postDeserialization(NetworkEvent.Context ctx, Level world) {
      super.postDeserialization(ctx, world);
      Connection.doClientSide(() -> {
         Entity patt4088$temp = this.getEntityAsReceptor(world);
         if (patt4088$temp instanceof EntityClientMessenger entity) {
            entity.clientCallOutHandler(this.flag, ObjectFetcher.of(this.clientObjects));
         }

      });
   }

   public LogicalSide side() {
      return LogicalSide.CLIENT;
   }

   public SimpleChannel mainChannel() {
      return BCPacketInitialization.CHANNEL;
   }
}
