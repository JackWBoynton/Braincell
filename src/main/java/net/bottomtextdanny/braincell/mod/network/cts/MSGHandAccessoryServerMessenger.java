package net.bottomtextdanny.braincell.mod.network.cts;

import net.bottomtextdanny.braincell.Braincell;
import net.bottomtextdanny.braincell.base.ObjectFetcher;
import net.bottomtextdanny.braincell.mod.network.BCPacket;
import net.bottomtextdanny.braincell.mod.network.Connection;
import net.bottomtextdanny.braincell.mod.network.OnlyHandledOnClient;
import net.bottomtextdanny.braincell.mod.network.OnlyHandledOnServer;
import net.bottomtextdanny.braincell.mod._base.serialization.SerializerMark;
import net.bottomtextdanny.braincell.mod._base.serialization.WorldPacketData;
import net.bottomtextdanny.braincell.mod._base.serialization.util.H_WorldDataParser;
import net.bottomtextdanny.braincell.mod.capability.BCCapabilityHelper;
import net.bottomtextdanny.braincell.mod.capability.player.BCAccessoryModule;
import net.bottomtextdanny.braincell.mod.capability.player.accessory.HandAccessories;
import net.bottomtextdanny.braincell.mod.network.BCPacketInitialization;
import net.minecraft.client.Minecraft;
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
    private WorldPacketData<?>[] packetObjects;
    @OnlyHandledOnServer
    private FriendlyByteBuf stream;
    @OnlyHandledOnServer
    private Object[] objects;

    public MSGHandAccessoryServerMessenger(FriendlyByteBuf stream) {
        this.handOrdinal = stream.readByte();
        this.flag = stream.readInt();
        this.stream = stream;
    }

    public MSGHandAccessoryServerMessenger(byte handOrdinal, int flag, WorldPacketData<?>[] objects) {
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
            if (this.packetObjects == null) {
                stream.writeInt(0);
            } else {

                int bound = Math.min(5, this.packetObjects.length);

                stream.writeInt(bound);

                for (int i = 0; i < bound; i++) {
                    WorldPacketData<?> object = this.packetObjects[i];
                    if (object != null) {
                        stream.writeInt(Braincell.common().getSerializerLookUp().getIdFromSerializer(object.getSerializer()));
                        object.writeToStream(stream, Minecraft.getInstance().level);
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
            final int size = this.stream.readInt();
            this.objects = new Object[size];
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    final int serializerId = this.stream.readInt();
                    final SerializerMark<?> serializer = Braincell.common().getSerializerLookUp().getSerializerFromId(serializerId);
                    if (serializer != null) {
                        this.objects[i] = H_WorldDataParser.readDataFromPacket(this.stream, serializer, world);
                    }
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
