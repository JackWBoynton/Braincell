package bottomtextdanny.braincell.libraries.serialization.builtin;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import bottomtextdanny.braincell.libraries.serialization.SimpleDataSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public final class DoubleSerializer implements DataSerializer<Double> {
    public static final ResourceLocation REF =
            new ResourceLocation(Braincell.ID, "double");

    @Override
    public void writeNBT(CompoundTag nbt, Double obj, String storage) {
        nbt.putDouble(storage, obj);
    }

    @Nullable
    @Override
    public Double readNBT(CompoundTag nbt, String storage) {
        if (!nbt.contains(storage)) return null;
        return nbt.getDouble(storage);
    }

    @Override
    public void writePacketStream(FriendlyByteBuf stream, Double obj) {
        stream.writeDouble(obj);
    }

    @Override
    public Double readPacketStream(FriendlyByteBuf stream) {
        return stream.readDouble();
    }

    @Override
    public ResourceLocation key() {
        return REF;
    }
}
