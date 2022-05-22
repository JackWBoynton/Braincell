package bottomtextdanny.braincell.mod._base.serialization.builtin;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.mod._base.serialization.SimpleSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public final class IntegerSerializer implements SimpleSerializer<Integer> {
    public static final ResourceLocation REF =
            new ResourceLocation(Braincell.ID, "integer");

    @Override
    public void writeNBT(CompoundTag nbt, Integer obj, String storage) {
        nbt.putInt(storage, obj);
    }

    @Nullable
    @Override
    public Integer readNBT(CompoundTag nbt, String storage) {
        if (!nbt.contains(storage)) return null;
        return nbt.getInt(storage);
    }

    @Override
    public void writePacketStream(FriendlyByteBuf stream, Integer obj) {
        stream.writeVarInt(obj);
    }

    @Override
    public Integer readPacketStream(FriendlyByteBuf stream) {
        return stream.readVarInt();
    }

    @Override
    public ResourceLocation key() {
        return REF;
    }
}
