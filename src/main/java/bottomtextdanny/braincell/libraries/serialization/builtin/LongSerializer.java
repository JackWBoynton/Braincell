package bottomtextdanny.braincell.libraries.serialization.builtin;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import bottomtextdanny.braincell.libraries.serialization.SimpleDataSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public final class LongSerializer implements DataSerializer<Long> {
    public static final ResourceLocation REF =
            new ResourceLocation(Braincell.ID, "long");

    @Override
    public void writeNBT(CompoundTag nbt, Long obj, String storage) {
        nbt.putLong(storage, obj);
    }

    @Nullable
    @Override
    public Long readNBT(CompoundTag nbt, String storage) {
        if (!nbt.contains(storage)) return null;
        return nbt.getLong(storage);
    }

    @Override
    public void writePacketStream(FriendlyByteBuf stream, Long obj) {
        stream.writeLong(obj);
    }

    @Override
    public Long readPacketStream(FriendlyByteBuf stream) {
        return stream.readLong();
    }

    @Override
    public ResourceLocation key() {
        return REF;
    }
}
