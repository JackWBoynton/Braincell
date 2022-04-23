package net.bottomtextdanny.braincell.mod.network.stc;

import io.netty.buffer.Unpooled;
import net.bottomtextdanny.braincell.Braincell;
import net.bottomtextdanny.braincell.mod._base.network.Connection;
import net.bottomtextdanny.braincell.mod.network.BCPacketInitialization;
import net.bottomtextdanny.braincell.mod._base.network.BCPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public class MSGComms implements BCPacket<MSGComms> {
    private final int id;
    private final FriendlyByteBuf dataStream;

    public MSGComms(int id, FriendlyByteBuf stream) {
        this.id = id;
        this.dataStream = stream;
    }

    @Override
    public void serialize(FriendlyByteBuf stream) {
        stream.writeVarInt(this.id);
        stream.writeBytes(this.dataStream);
    }

    @Override
    public MSGComms deserialize(FriendlyByteBuf stream) {
        int id = stream.readVarInt();
        return new MSGComms(id, new FriendlyByteBuf(Unpooled.wrappedBuffer(stream.readBytes(stream.readableBytes()))));
    }

    @Override
    public void postDeserialization(NetworkEvent.Context ctx, Level world) {
        Connection.doClientSide(() -> {
            Braincell.client().setComms(this.id, this.dataStream);
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
