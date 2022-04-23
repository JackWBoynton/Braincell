package net.bottomtextdanny.braincell.mod.network;

import net.bottomtextdanny.braincell.Braincell;
import net.bottomtextdanny.braincell.mod.network.cts.MSGHandAccessoryServerMessenger;
import net.bottomtextdanny.braincell.mod.network.stc.*;
import net.bottomtextdanny.braincell.mod._base.network.BCPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.commons.lang3.mutable.MutableInt;

public final class BCPacketInitialization {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Braincell.ID, "main"),
            () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void initializeNetworkPackets() {
        MutableInt id = new MutableInt(0);
        initializeServerNetworkPackets(id);
        initializeClientNetworkPackets(id);
    }

    private static void initializeClientNetworkPackets(MutableInt token) {
        register(token, new MSGBlockEntityClientMessenger(null, -1, null, null));
        register(token, new MSGEntityAnimation(-1, -1, -1));
        register(token, new MSGEntityClientMessenger(-1, -1, null, null));
        register(token, new MSGUpdateDataManager(-1, null));
        register(token, new MSGHandAccessoryClientMessenger(-1));
        register(token, new MSGTrivialEntityActions(-1, -1));
        register(token, new MSGComms(-1, (FriendlyByteBuf) null));
    }

    private static void initializeServerNetworkPackets(MutableInt token) {
        register(token, new MSGHandAccessoryServerMessenger((byte)-1, -1, null));
    }

    @SuppressWarnings("unchecked")
    private static <T extends BCPacket<T>> T register(MutableInt id, T packet) {
        CHANNEL.registerMessage(
                id.getAndIncrement(),
                (Class<T>) packet.getClass(),
                packet::_serializeHandler,
                packet::deserialize,
                packet::_postDeserializationHandler);
        return packet;
    }

    public static void sendPlayerVelocityPacket(Entity player) {
        if (player instanceof ServerPlayer) {
            ((ServerPlayer)player).connection.send(new ClientboundSetEntityMotionPacket(player));
        }
    }
}
