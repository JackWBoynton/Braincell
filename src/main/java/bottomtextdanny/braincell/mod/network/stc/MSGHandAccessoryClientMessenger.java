package bottomtextdanny.braincell.mod.network.stc;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.mod._base.serialization.SerializerMark;
import bottomtextdanny.braincell.mod._base.serialization.WorldPacketData;
import bottomtextdanny.braincell.mod.capability.BCCapabilityHelper;
import bottomtextdanny.braincell.mod.capability.player.BCAccessoryModule;
import bottomtextdanny.braincell.mod.capability.player.accessory.HandAccessories;
import bottomtextdanny.braincell.mod.network.BCEntityPacket;
import bottomtextdanny.braincell.mod.network.Connection;
import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.mod._base.serialization.util.H_WorldDataParser;
import bottomtextdanny.braincell.mod.network.BCPacketInitialization;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.commons.lang3.mutable.MutableObject;

public final class MSGHandAccessoryClientMessenger extends BCEntityPacket<MSGHandAccessoryClientMessenger, Player> {
    private byte handOrdinal;
    private int flag;
    private WorldPacketData<?>[] packetObjects;
    private Object[] objects;
    private ServerLevel level;

    public MSGHandAccessoryClientMessenger(int playerId) {
        super(playerId);
    }

    public MSGHandAccessoryClientMessenger(
            int entityId,
            byte handOrdinal,
            int flag,
            WorldPacketData<?>[] objects,
            ServerLevel level) {
        super(entityId);
        this.handOrdinal = handOrdinal;
        this.flag = flag;
        this.packetObjects = objects;
        this.level = level;
    }

    @Override
    public void serialize(FriendlyByteBuf stream) {
        super.serialize(stream);
        stream.writeByte(this.handOrdinal);
        stream.writeInt(this.flag);

        if (this.packetObjects == null) {
            stream.writeInt(0);
        } else {
            int bound = this.packetObjects.length;

            stream.writeInt(bound);

            for (int i = 0; i < bound; i++) {
                WorldPacketData<?> object = this.packetObjects[i];
                if (object != null) {
                    stream.writeInt(Braincell.common().getSerializerLookUp().getIdFromSerializer(object.getSerializer()));
                    object.writeToStream(stream, this.level);
                } else {
                    stream.writeInt(-1);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public MSGHandAccessoryClientMessenger deserialize(FriendlyByteBuf stream) {
        MutableObject<MSGHandAccessoryClientMessenger> msg = new MutableObject(null);
        Connection.doClientSide(() -> {
            msg.setValue(new MSGHandAccessoryClientMessenger(stream.readInt()));

            msg.getValue().handOrdinal = stream.readByte();
            msg.getValue().flag = stream.readInt();

            final int size = stream.readInt();
            msg.getValue().objects = new Object[size];

            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    final int serializerId = stream.readInt();
                    final SerializerMark<?> serializer = Braincell.common().getSerializerLookUp().getSerializerFromId(serializerId);

                    if (serializer != null) {
                        msg.getValue().objects[i] = H_WorldDataParser.readDataFromPacket(stream, serializer, Minecraft.getInstance().level);
                    }
                }
            }
        });
        return msg.getValue();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void postDeserialization(NetworkEvent.Context ctx, Level world) {
        Player player = getEntityAsReceptor(world);
        Connection.doClientSide(() -> {
            if (player != null && player.isAlive()) {
                BCAccessoryModule accessoryModule = BCCapabilityHelper.accessoryModule(player);
                HandAccessories.ACCESSORY_FOR_HAND.get(this.handOrdinal).apply(accessoryModule).accessoryClientManager(this.flag, ObjectFetcher.of(this.objects));
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
