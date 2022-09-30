package bottomtextdanny.braincell.libraries.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public interface BCPacket<T extends BCPacket<T>> {

    void serialize(FriendlyByteBuf stream);

    T deserialize(FriendlyByteBuf stream);

    default void postDeserialization(NetworkEvent.Context ctx, Level world) {}

    LogicalSide side();

    default void sendToServer() {
        if (side() == LogicalSide.CLIENT)
            throw new IllegalStateException("Attempted to send a client-configured packet to server.");

        mainChannel().sendToServer(this);
    }

    default void sendTo(PacketDistributor.PacketTarget target) {
        if (side() == LogicalSide.SERVER)
            throw new IllegalStateException("Attempted to send a server-configured packet to client.");

        mainChannel().send(target, this);
    }

    SimpleChannel mainChannel();

    @Nullable
    default Level worldProvider(NetworkEvent.Context ctx) {
        if (side() == LogicalSide.SERVER) return ctx.getSender().level;
        else {
            return (Level) Connection.makeClientSideUnknown(() -> {
                return Minecraft.getInstance().level;
            });
        }
    }

    default void _serializeHandler(T packet, FriendlyByteBuf stream) {
        packet.serialize(stream);
    }

    default void _postDeserializationHandler(T packet, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide() == side()) {
            Level world = packet.worldProvider(ctx.get());
            if (world != null) {
                packet.postDeserialization(ctx.get(), world);
            }
        }
    }
}
