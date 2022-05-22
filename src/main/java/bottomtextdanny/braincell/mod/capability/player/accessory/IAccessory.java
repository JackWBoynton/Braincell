package bottomtextdanny.braincell.mod.capability.player.accessory;

import bottomtextdanny.braincell.mod._base.serialization.WorldPacketData;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;

public interface IAccessory {
    IAccessory EMPTY = new EmptyAccessory();

    void tick();

    @OnlyIn(Dist.CLIENT)
    void keyHandler(Options settings);

    @OnlyIn(Dist.CLIENT)
    void frameTick(LocalPlayer cPlayer, PoseStack poseStack, float partialTicks);

    void triggerClientAction(int flag, PacketDistributor.PacketTarget target, WorldPacketData<?>... data);

    void triggerClientActionToTracking(int flag, WorldPacketData<?>... data);

    void triggerClientActionToTracking(int flag);

    void triggerServerAction(int flag, WorldPacketData<?>... data);

    void triggerServerAction(int flag);

    AccessoryKey<?> getKey();

    void read(CompoundTag nbt);

    CompoundTag write();

    class EmptyAccessory implements IAccessory {

        @Override
        public void tick() {}

        @Override
        public void keyHandler(Options settings) {}

        @Override
        public void frameTick(LocalPlayer cPlayer, PoseStack poseStack, float partialTicks) {}

        @Override
        public void triggerClientAction(int flag, PacketDistributor.PacketTarget target, WorldPacketData<?>... data) {}

        @Override
        public void triggerClientActionToTracking(int flag, WorldPacketData<?>... data) {}

        @Override
        public void triggerClientActionToTracking(int flag) {}

        @Override
        public void triggerServerAction(int flag, WorldPacketData<?>... data) {}

        @Override
        public void triggerServerAction(int flag) {}

        @Override
        public AccessoryKey<?> getKey() {
            return BCAccessoryKeys.EMPTY;
        }

        @Override
        public void read(CompoundTag nbt) {}

        @Override
        public CompoundTag write() {
            return new CompoundTag();
        }
    }
}
