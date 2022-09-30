package bottomtextdanny.braincell.libraries.serialization.builtin;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import bottomtextdanny.braincell.libraries.serialization.SimpleDataSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public final class ByteSerializer implements DataSerializer<Byte> {
    public static final ResourceLocation REF =
            new ResourceLocation(Braincell.ID, "byte");

    @Override
    public void writeNBT(CompoundTag nbt, Byte obj, String storage) {
        nbt.putByte(storage, obj);
    }

    @Nullable
    @Override
    public Byte readNBT(CompoundTag nbt, String storage) {
        if (!nbt.contains(storage)) return null;
        return nbt.getByte(storage);
    }

    @Override
    public void writePacketStream(FriendlyByteBuf stream, Byte obj) {
        stream.writeByte(obj);
    }

    @Override
    public Byte readPacketStream(FriendlyByteBuf stream) {
        return stream.readByte();
    }

    @Override
    public ResourceLocation key() {
        return REF;
    }
}
