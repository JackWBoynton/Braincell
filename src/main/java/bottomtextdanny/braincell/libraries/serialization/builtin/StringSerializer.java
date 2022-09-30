package bottomtextdanny.braincell.libraries.serialization.builtin;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import bottomtextdanny.braincell.libraries.serialization.SimpleDataSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public final class StringSerializer implements DataSerializer<String> {
    public static final ResourceLocation REF =
            new ResourceLocation(Braincell.ID, "string");

    @Override
    public void writeNBT(CompoundTag nbt, String obj, String storage) {
        nbt.putString(storage, obj);
    }

    @Nullable
    @Override
    public String readNBT(CompoundTag nbt, String storage) {
        if (!nbt.contains(storage)) return null;
        return nbt.getString(storage);
    }

    @Override
    public void writePacketStream(FriendlyByteBuf stream, String obj) {
        stream.writeUtf(obj);
    }

    @Override
    public String readPacketStream(FriendlyByteBuf stream) {
        return stream.readUtf();
    }

    @Override
    public ResourceLocation key() {
        return REF;
    }
}
