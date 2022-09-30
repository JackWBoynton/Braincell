package bottomtextdanny.braincell;

import bottomtextdanny.braincell.libraries._minor.entity.MSGTrivialEntityActions;
import bottomtextdanny.braincell.libraries.entity_animation.MSGAnimatedEntityDeathEnd;
import bottomtextdanny.braincell.libraries.entity_animation.MSGEntityAnimation;
import bottomtextdanny.braincell.libraries.accessory.MSGHandAccessoryClientMessenger;
import bottomtextdanny.braincell.libraries.accessory.MSGHandAccessoryServerMessenger;
import bottomtextdanny.braincell.libraries._minor.MSGComms;
import bottomtextdanny.braincell.libraries._minor.block_entity_messenger.MSGBlockEntityClientMessenger;
import bottomtextdanny.braincell.libraries._minor.block_entity_messenger.MSGEntityClientMessenger;
import bottomtextdanny.braincell.libraries._minor.entity_data_manager.MSGUpdateDataManager;
import bottomtextdanny.braincell.libraries.flagged_schema_block.MSGUpdateClientFlaggedSchemaBlock;
import bottomtextdanny.braincell.libraries.flagged_schema_block.MSGUpdateServerFlaggedSchemaBlock;
import bottomtextdanny.braincell.libraries.network.BCPacket;
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
        register(token, new MSGAnimatedEntityDeathEnd(-1));
        register(token, new MSGUpdateClientFlaggedSchemaBlock(-1L, -1, -1, -1, -1, -1, -1, -1, -1, -1));
    }

    private static void initializeServerNetworkPackets(MutableInt token) {
        register(token, new MSGHandAccessoryServerMessenger((byte)-1, -1, null));
        register(token, new MSGUpdateServerFlaggedSchemaBlock(-1L, -1, -1, -1, -1, -1, -1, -1, -1, -1));
    }

    @SuppressWarnings("unchecked")
    private static <T extends BCPacket<T>> T register(MutableInt id, T packet) {
        CHANNEL.messageBuilder((Class<T>)packet.getClass(), id.getAndIncrement())
                .encoder(packet::_serializeHandler)
                .decoder(packet::deserialize)
                .consumerMainThread(packet::_postDeserializationHandler).add();

        return packet;
    }

    public static void sendPlayerVelocityPacket(Entity player) {
        if (player instanceof ServerPlayer) {
            ((ServerPlayer)player).connection.send(new ClientboundSetEntityMotionPacket(player));
        }
    }
}
