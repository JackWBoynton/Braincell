package bottomtextdanny.braincell.libraries.serialization.builtin;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import bottomtextdanny.braincell.libraries.serialization.SimpleDataSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public final class ShortSerializer implements DataSerializer<Short> {
    public static final ResourceLocation REF =
            new ResourceLocation(Braincell.ID, "short");

    @Override
    public void writeNBT(CompoundTag nbt, Short obj, String storage) {
        nbt.putShort(storage, obj);
    }

    @Nullable
    @Override
    public Short readNBT(CompoundTag nbt, String storage) {
        if (!nbt.contains(storage)) return null;
        return nbt.getShort(storage);
    }

    @Override
    public void writePacketStream(FriendlyByteBuf stream, Short obj) {
        stream.writeShort(obj);
    }

    @Override
    public Short readPacketStream(FriendlyByteBuf stream) {
        return stream.readShort();
    }

    @Override
    public ResourceLocation key() {
        return REF;
    }
}
