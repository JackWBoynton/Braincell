package bottomtextdanny.braincell.mod._base.serialization.builtin;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.mod._base.serialization.SimpleSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public final class ByteSerializer implements SimpleSerializer<Byte> {
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
