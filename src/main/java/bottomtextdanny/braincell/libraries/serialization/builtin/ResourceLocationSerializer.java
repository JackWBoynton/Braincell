package bottomtextdanny.braincell.libraries.serialization.builtin;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import bottomtextdanny.braincell.libraries.serialization.SimpleDataSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public final class ResourceLocationSerializer implements DataSerializer<ResourceLocation> {
    public static final ResourceLocation REF =
            new ResourceLocation(Braincell.ID, "resource_location");

    @Override
    public void writeNBT(CompoundTag nbt, ResourceLocation obj, String storage) {
        nbt.putString(storage, obj.toString());
    }

    @Nullable
    @Override
    public ResourceLocation readNBT(CompoundTag nbt, String storage) {
        if (!nbt.contains(storage)) return null;
        return new ResourceLocation(nbt.getString(storage));
    }

    @Override
    public void writePacketStream(FriendlyByteBuf stream, ResourceLocation obj) {
        stream.writeUtf(obj.toString());
    }

    @Override
    public ResourceLocation readPacketStream(FriendlyByteBuf stream) {
        return new ResourceLocation(stream.readUtf());
    }

    @Override
    public ResourceLocation key() {
        return REF;
    }
}
