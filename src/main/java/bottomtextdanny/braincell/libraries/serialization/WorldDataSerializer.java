package bottomtextdanny.braincell.libraries.serialization;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public interface WorldDataSerializer<T> extends DataSerializer<T> {
    Logger LOGGER = LogManager.getLogger();

    void writeNBT(CompoundTag nbt, T obj, Level level, String storage);

    @Nullable
    T readNBT(CompoundTag nbt, Level level, String storage);

    void writePacketStream(FriendlyByteBuf stream, Level level, T obj);

    T readPacketStream(FriendlyByteBuf stream, Level level);
}
