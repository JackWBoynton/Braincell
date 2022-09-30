package bottomtextdanny.braincell.libraries.serialization.builtin;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import bottomtextdanny.braincell.libraries.serialization.SimpleDataSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public final class FloatSerializer implements DataSerializer<Float> {
    public static final ResourceLocation REF =
            new ResourceLocation(Braincell.ID, "float");

    @Override
    public void writeNBT(CompoundTag nbt, Float obj, String storage) {
        nbt.putFloat(storage, obj);
    }

    @Nullable
    @Override
    public Float readNBT(CompoundTag nbt, String storage) {
        if (!nbt.contains(storage)) return null;
        return nbt.getFloat(storage);
    }

    @Override
    public void writePacketStream(FriendlyByteBuf stream, Float obj) {
        stream.writeFloat(obj);
    }

    @Override
    public Float readPacketStream(FriendlyByteBuf stream) {
        return stream.readFloat();
    }

    @Override
    public ResourceLocation key() {
        return REF;
    }
}
