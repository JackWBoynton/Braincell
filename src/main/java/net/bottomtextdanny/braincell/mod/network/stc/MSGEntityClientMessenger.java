package net.bottomtextdanny.braincell.mod.network.stc;

import net.bottomtextdanny.braincell.Braincell;
import net.bottomtextdanny.braincell.mod._base.network.OnlyHandledOnClient;
import net.bottomtextdanny.braincell.mod._base.network.OnlyHandledOnServer;
import net.bottomtextdanny.braincell.mod._base.network.Connection;
import net.bottomtextdanny.braincell.mod.network.BCPacketInitialization;
import net.bottomtextdanny.braincell.mod._base.network.BCEntityPacket;
import net.bottomtextdanny.braincell.mod._base.serialization.WorldPacketData;
import net.bottomtextdanny.braincell.mod._base.serialization.SerializerMark;
import net.bottomtextdanny.braincell.mod._base.serialization.util.H_WorldDataParser;
import net.bottomtextdanny.braincell.mod.world.entity_utilities.EntityClientMessenger;
import net.bottomtextdanny.braincell.base.ObjectFetcher;
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

import javax.annotation.Nullable;

public final class MSGEntityClientMessenger extends BCEntityPacket<MSGEntityClientMessenger, Entity> {
    private final int flag;
    @OnlyHandledOnServer private WorldPacketData<?>[] serverObjects;
    @OnlyHandledOnServer private ServerLevel serverLevel;
    @OnlyHandledOnClient private Object[] clientObjects;

    @OnlyIn(Dist.CLIENT)
    public MSGEntityClientMessenger(FriendlyByteBuf stream) {
        super(stream.readInt());
        this.flag = stream.readInt();
        final int size = stream.readInt();
        this.clientObjects = new Object[size];

        Connection.doClientSide(() -> {
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    final int serializerId = stream.readInt();
                    if (serializerId == -1) continue;
                    final SerializerMark<?> serializer =
                            Braincell.common().getSerializerLookUp().getSerializerFromId(serializerId);
                    if (serializer != null) {
                        this.clientObjects[i] = H_WorldDataParser.readDataFromPacket(
                                stream,
                                serializer,
                                Minecraft.getInstance().level);
                    }
                }
            }
        });
    }

    public MSGEntityClientMessenger(int entityId, int flag, ServerLevel level, @Nullable WorldPacketData<?>[] objects) {
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
            stream.writeInt(this.serverObjects.length);

            for (WorldPacketData<?> object : this.serverObjects) {

                if (object != null) {
                    stream.writeInt(Braincell.common().getSerializerLookUp().getIdFromSerializer(
                            object.getSerializer()));
                    object.writeToStream(stream, this.serverLevel);
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
