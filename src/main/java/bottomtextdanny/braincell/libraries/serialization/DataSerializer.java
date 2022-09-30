package bottomtextdanny.braincell.libraries.serialization;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public interface DataSerializer<T> {
    Logger LOGGER = LogManager.getLogger();

    void writeNBT(CompoundTag nbt, T obj, String storage);

    @Nullable
    T readNBT(CompoundTag nbt, String storage);

    void writePacketStream(FriendlyByteBuf stream, T obj);

    T readPacketStream(FriendlyByteBuf stream);

    ResourceLocation key();
}
