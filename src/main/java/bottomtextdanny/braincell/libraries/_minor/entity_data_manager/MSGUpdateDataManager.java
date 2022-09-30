package bottomtextdanny.braincell.libraries._minor.entity_data_manager;

import bottomtextdanny.braincell.libraries.network.BCEntityPacket;
import bottomtextdanny.braincell.BCPacketInitialization;
import bottomtextdanny.braincell.libraries.network.Connection;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public final class MSGUpdateDataManager extends BCEntityPacket<MSGUpdateDataManager, Entity> {
    private final BCDataManager manager;
    private final FriendlyByteBuf stream;

    public MSGUpdateDataManager(int entityId, BCDataManager manager, FriendlyByteBuf stream) {
        super(entityId);
        this.manager = manager;
        this.stream = stream;
    }

    public MSGUpdateDataManager(int entityId, BCDataManager manager) {
        super(entityId);
        this.manager = manager;
        this.stream = null;
    }

    @Override
    public void serialize(FriendlyByteBuf stream) {
        super.serialize(stream);
        this.manager.writePacket(stream);
    }

    @Override
    public MSGUpdateDataManager deserialize(FriendlyByteBuf stream) {
        return new MSGUpdateDataManager(stream.readInt(), null, stream);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void postDeserialization(NetworkEvent.Context ctx, Level world) {
        super.postDeserialization(ctx, world);
        Connection.doClientSide(() -> {
            if (Minecraft.getInstance().player != null) {
                if (getEntityAsReceptor(Minecraft.getInstance().player.level) instanceof BCDataManagerProvider dataHolder) {
                    dataHolder.bcDataManager().readPacket(stream);
                    dataHolder.afterClientDataUpdate();

                }
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
