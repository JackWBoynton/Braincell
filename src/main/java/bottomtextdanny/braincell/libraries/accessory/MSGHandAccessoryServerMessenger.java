package bottomtextdanny.braincell.libraries.accessory;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import bottomtextdanny.braincell.libraries.serialization.PacketData;
import bottomtextdanny.braincell.libraries.capability.BCCapabilityHelper;
import bottomtextdanny.braincell.libraries.network.Connection;
import bottomtextdanny.braincell.libraries.network.OnlyHandledOnClient;
import bottomtextdanny.braincell.libraries.network.OnlyHandledOnServer;
import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.libraries.network.BCPacket;
import bottomtextdanny.braincell.BCPacketInitialization;
import bottomtextdanny.braincell.libraries.serialization.SerializerLookup;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public final class MSGHandAccessoryServerMessenger implements BCPacket<MSGHandAccessoryServerMessenger> {
    private final byte handOrdinal;
    private final int flag;
    @OnlyHandledOnClient
    private PacketData<?>[] packetObjects;
    @OnlyHandledOnServer
    private FriendlyByteBuf stream;
    @OnlyHandledOnServer
    private Object[] objects;

    public MSGHandAccessoryServerMessenger(FriendlyByteBuf stream) {
        this.handOrdinal = stream.readByte();
        this.flag = stream.readInt();
        this.stream = stream;
    }

    public MSGHandAccessoryServerMessenger(byte handOrdinal, int flag, PacketData<?>[] objects) {
        this.handOrdinal = handOrdinal;
        this.flag = flag;
        this.packetObjects = objects;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void serialize(FriendlyByteBuf stream) {
        stream.writeByte(this.handOrdinal);
        stream.writeInt(this.flag);

        Connection.doClientSide(() -> {
            PacketData<?>[] packetObjects = this.packetObjects;
            if (packetObjects == null) {
                stream.writeInt(0);
            } else {
                int bound = Math.min(5, packetObjects.length);

                stream.writeInt(bound);

                SerializerLookup serializerLookUp = Braincell.common().getSerializerLookUp();

                for (int i = 0; i < bound; i++) {
                    PacketData<?> object = packetObjects[i];
                    if (object != null) {
                        stream.writeInt(serializerLookUp.getIdFromSerializer(object.getSerializer()));
                        object.writeToStream(stream);
                    } else {
                        stream.writeInt(-1);
                    }
                }
            }
        });
    }

    @Override
    public MSGHandAccessoryServerMessenger deserialize(FriendlyByteBuf stream) {
        return new MSGHandAccessoryServerMessenger(stream);
    }

    @Override
    public void postDeserialization(NetworkEvent.Context ctx, Level world) {
        ServerPlayer player = ctx.getSender();
        if (player != null) {
            FriendlyByteBuf stream = this.stream;
            final int size = stream.readInt();
            this.objects = new Object[size];
            if (size > 0) {
                Object[] objects = this.objects;
                for (int i = 0; i < size; i++) {
                    final int serializerId = stream.readInt();
                    final DataSerializer<?> serializer = Braincell.common().getSerializerLookUp().getSerializerFromId(serializerId);
                    if (serializer != null)
                        objects[i] = serializer.readPacketStream(stream);
                }
            }

            final BCAccessoryModule accessoryModule = BCCapabilityHelper.accessoryModule(player);

            if (this.handOrdinal >= 0 && this.handOrdinal < 2) {

                HandAccessories.ACCESSORY_FOR_HAND.get(this.handOrdinal).apply(accessoryModule).accessoryServerManager(this.flag, ObjectFetcher.of(this.objects));
            }
        }
    }

    @Override
    public LogicalSide side() {
        return LogicalSide.SERVER;
    }

    @Override
    public SimpleChannel mainChannel() {
        return BCPacketInitialization.CHANNEL;
    }
}
