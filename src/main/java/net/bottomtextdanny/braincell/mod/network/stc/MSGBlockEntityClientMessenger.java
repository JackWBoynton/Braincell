package net.bottomtextdanny.braincell.mod.network.stc;

import net.bottomtextdanny.braincell.Braincell;
import net.bottomtextdanny.braincell.base.ObjectFetcher;
import net.bottomtextdanny.braincell.mod.network.BCPacket;
import net.bottomtextdanny.braincell.mod.network.Connection;
import net.bottomtextdanny.braincell.mod.network.OnlyHandledOnClient;
import net.bottomtextdanny.braincell.mod.network.OnlyHandledOnServer;
import net.bottomtextdanny.braincell.mod._base.serialization.SerializerMark;
import net.bottomtextdanny.braincell.mod._base.serialization.WorldPacketData;
import net.bottomtextdanny.braincell.mod._base.serialization.util.H_WorldDataParser;
import net.bottomtextdanny.braincell.mod.network.BCPacketInitialization;
import net.bottomtextdanny.braincell.mod.world.block_entity_utilities.BlockEntityClientMessenger;
import net.minecraft.client.Minecraft;
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
    @OnlyHandledOnServer private WorldPacketData<?>[] serverObjects;
    @OnlyHandledOnServer private ServerLevel serverLevel;
    @OnlyHandledOnClient private Object[] clientObjects;

    @OnlyIn(Dist.CLIENT)
    public MSGBlockEntityClientMessenger(FriendlyByteBuf stream) {
        this.pos = BlockPos.of(stream.readLong());
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

    public MSGBlockEntityClientMessenger(BlockPos blockPosition, int flag, ServerLevel level, @Nullable WorldPacketData<?>[] objects) {
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
