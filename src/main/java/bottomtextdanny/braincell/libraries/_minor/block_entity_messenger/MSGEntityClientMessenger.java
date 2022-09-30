package bottomtextdanny.braincell.libraries._minor.block_entity_messenger;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.serialization.SerializerLookup;
import bottomtextdanny.braincell.libraries.serialization.PacketData;
import bottomtextdanny.braincell.libraries.network.Connection;
import bottomtextdanny.braincell.libraries.network.OnlyHandledOnClient;
import bottomtextdanny.braincell.libraries.network.OnlyHandledOnServer;
import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.libraries.network.BCEntityPacket;
import bottomtextdanny.braincell.BCPacketInitialization;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import javax.annotation.Nullable;

public final class MSGEntityClientMessenger extends BCEntityPacket<MSGEntityClientMessenger, Entity> {
    private final int flag;
    @OnlyHandledOnServer
    private PacketData<?>[] serverObjects;
    @OnlyHandledOnServer private ServerLevel serverLevel;
    @OnlyHandledOnClient
    private Object[] clientObjects;

    @OnlyIn(Dist.CLIENT)
    public MSGEntityClientMessenger(FriendlyByteBuf stream) {
        super(stream.readInt());
        this.flag = stream.readInt();
        final int size = stream.readInt();
        Object[] clientObjects = new Object[size];
        this.clientObjects = clientObjects;

        Connection.doClientSide(() -> {
            if (size > 0) {
                SerializerLookup serializerLookUp = Braincell.common().getSerializerLookUp();
                for (int i = 0; i < size; i++) {
                    final int serializerId = stream.readInt();
                    if (serializerId == -1) continue;
                    clientObjects[i] = serializerLookUp.getSerializerFromId(serializerId).readPacketStream(stream);
                }
            }
        });
    }

    public MSGEntityClientMessenger(int entityId, int flag, ServerLevel level, @Nullable PacketData<?>[] objects) {
        super(entityId);
        this.flag = flag;
        this.serverObjects = objects;
        this.serverLevel = level;
    }

    @Override
    public void serialize(FriendlyByteBuf stream) {
        super.serialize(stream);
        stream.writeInt(this.flag);

        if (this.serverObjects == null) {
            stream.writeInt(0);
        } else {
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
    public MSGEntityClientMessenger deserialize(FriendlyByteBuf stream) {
        return new MSGEntityClientMessenger(stream);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void postDeserialization(NetworkEvent.Context ctx, Level world) {
        super.postDeserialization(ctx, world);
        Connection.doClientSide(() -> {
            if (getEntityAsReceptor(world) instanceof EntityClientMessenger entity) {
                entity.clientCallOutHandler(this.flag, ObjectFetcher.of(this.clientObjects));
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
