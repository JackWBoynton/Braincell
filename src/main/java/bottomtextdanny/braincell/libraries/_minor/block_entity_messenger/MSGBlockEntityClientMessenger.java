package bottomtextdanny.braincell.libraries._minor.block_entity_messenger;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.serialization.SerializerLookup;
import bottomtextdanny.braincell.libraries.serialization.PacketData;
import bottomtextdanny.braincell.BCPacketInitialization;
import bottomtextdanny.braincell.libraries.network.Connection;
import bottomtextdanny.braincell.libraries.network.OnlyHandledOnClient;
import bottomtextdanny.braincell.libraries.network.OnlyHandledOnServer;
import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.libraries.network.BCPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import javax.annotation.Nullable;

public final class MSGBlockEntityClientMessenger implements BCPacket<MSGBlockEntityClientMessenger> {
    private final BlockPos pos;
    private final int flag;
    @OnlyHandledOnServer
    private PacketData<?>[] serverObjects;
    @OnlyHandledOnServer private ServerLevel serverLevel;
    @OnlyHandledOnClient
    private Object[] clientObjects;

    @OnlyIn(Dist.CLIENT)
    public MSGBlockEntityClientMessenger(FriendlyByteBuf stream) {
        this.pos = BlockPos.of(stream.readLong());
        this.flag = stream.readInt();
        final int size = stream.readInt();
        Object[] clientObjects = new Object[size];
        this.clientObjects = clientObjects;

        if (size > 0) {
            SerializerLookup serializerLookUp = Braincell.common().getSerializerLookUp();
            for (int i = 0; i < size; i++) {
                final int serializerId = stream.readInt();

                if (serializerId == -1) continue;

                clientObjects[i] = serializerLookUp.getSerializerFromId(serializerId).readPacketStream(stream);
            }
        }
    }

    public MSGBlockEntityClientMessenger(BlockPos blockPosition, int flag, ServerLevel level, @Nullable PacketData<?>[] objects) {
        this.pos = blockPosition;
        this.flag = flag;
        this.serverObjects = objects;
        this.serverLevel = level;
    }

    @Override
    public void serialize(FriendlyByteBuf stream) {
        stream.writeLong(this.pos.asLong());
        stream.writeInt(this.flag);

        if (this.serverObjects == null) {
            stream.writeInt(0);
        } else {
            ServerLevel serverLevel = this.serverLevel;
            SerializerLookup serializerLookUp = Braincell.common().getSerializerLookUp();
            stream.writeInt(this.serverObjects.length);

            for (PacketData<?> object : this.serverObjects) {

                if (object != null) {
                    stream.writeInt(serializerLookUp.getIdFromSerializer(
                            object.getSerializer()));

                    object.writeToStream(stream);
                } else {
                    stream.writeInt(-1);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public MSGBlockEntityClientMessenger deserialize(FriendlyByteBuf stream) {
        return new MSGBlockEntityClientMessenger(stream);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void postDeserialization(NetworkEvent.Context ctx, Level world) {
        Connection.doClientSide(() -> {
            if (world.getBlockEntity(this.pos) instanceof BlockEntityClientMessenger blockEntity) {
                blockEntity.clientCallOutHandler(this.flag, ObjectFetcher.of(this.clientObjects));
            }
        });
    }

    @Override
    public LogicalSide side() {
        return LogicalSide.CLIENT;
    }

    @Override
    public SimpleChannel mainChannel() {
        return BCPacketInitialization.CHANNEL;
    }
}
