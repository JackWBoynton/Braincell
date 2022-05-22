package bottomtextdanny.braincell.mod._base.serialization;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public interface SimpleSerializer<T> extends SerializerMark<T> {
    Logger LOGGER = LogManager.getLogger();

    void writeNBT(CompoundTag nbt, T obj, String storage);

    @Nullable
    T readNBT(CompoundTag nbt, String storage);

    void writePacketStream(FriendlyByteBuf stream, T obj);

    T readPacketStream(FriendlyByteBuf stream);
}
