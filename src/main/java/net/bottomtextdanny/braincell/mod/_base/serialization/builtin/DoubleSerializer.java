package net.bottomtextdanny.braincell.mod._base.serialization.builtin;

import net.bottomtextdanny.braincell.Braincell;
import net.bottomtextdanny.braincell.mod._base.serialization.SimpleSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public final class DoubleSerializer implements SimpleSerializer<Double> {
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
