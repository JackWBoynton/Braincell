package bottomtextdanny.braincell.libraries._minor.block_entity_messenger;

import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.libraries.serialization.PacketData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;

public interface BlockEntityClientMessenger {

    @OnlyIn(Dist.CLIENT)
    default void clientCallOutHandler(int flag, ObjectFetcher fetcher) {}

    default void sendClientMsg(int flag, @Nullable PacketData<?>... o) {
        this.sendClientMsg(flag, PacketDistributor.TRACKING_ENTITY.with(() -> (Entity) this), o);
    }

    default void sendClientMsg(int flag, PacketDistributor.PacketTarget distributor, @Nullable PacketData<?>... o) {
        BlockEntity asBlockEntity = (BlockEntity) this;
        if (asBlockEntity.getLevel() instanceof ServerLevel level) {
            new MSGBlockEntityClientMessenger(asBlockEntity.getBlockPos(), flag, level, o).sendTo(distributor);
        }
    }

    @Deprecated
    default void sendClientMsg(int flag) {
        this.sendClientMsg(flag, (PacketData<?>) null);
    }
}
