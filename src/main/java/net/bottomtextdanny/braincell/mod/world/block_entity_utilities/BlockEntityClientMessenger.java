package net.bottomtextdanny.braincell.mod.world.block_entity_utilities;

import net.bottomtextdanny.braincell.mod.network.stc.MSGBlockEntityClientMessenger;
import net.bottomtextdanny.braincell.mod._base.serialization.WorldPacketData;
import net.bottomtextdanny.braincell.base.ObjectFetcher;
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

    default void sendClientMsg(int flag, @Nullable WorldPacketData<?>... o) {
        this.sendClientMsg(flag, PacketDistributor.TRACKING_ENTITY.with(() -> (Entity) this), o);
    }

    default void sendClientMsg(int flag, PacketDistributor.PacketTarget distributor, @Nullable WorldPacketData<?>... o) {
        BlockEntity asBlockEntity = (BlockEntity) this;
        if (asBlockEntity.getLevel() instanceof ServerLevel level) {
            new MSGBlockEntityClientMessenger(asBlockEntity.getBlockPos(), flag, level, o).sendTo(distributor);
        }
    }

    @Deprecated
    default void sendClientMsg(int flag) {
        this.sendClientMsg(flag, (WorldPacketData<?>) null);
    }
}
