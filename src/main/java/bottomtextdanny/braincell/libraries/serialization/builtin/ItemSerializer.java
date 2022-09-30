package bottomtextdanny.braincell.libraries.serialization.builtin;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import bottomtextdanny.braincell.libraries.serialization.SimpleDataSerializer;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;

public final class ItemSerializer implements DataSerializer<Item> {
    public static final ResourceLocation REF =
            new ResourceLocation(Braincell.ID, "item");

    @Override
    public void writeNBT(CompoundTag nbt, Item obj, String storage) {
        nbt.putString(storage, obj.builtInRegistryHolder().key().location().toString());
    }

    @Nullable
    @Override
    public Item readNBT(CompoundTag nbt, String storage) {
        if (!nbt.contains(storage)) return null;
        return Registry.ITEM.get(new ResourceLocation(nbt.getString(storage)));
    }

    @Override
    public void writePacketStream(FriendlyByteBuf stream, Item obj) {
        stream.writeUtf(obj.builtInRegistryHolder().key().location().toString());
    }

    @Override
    public Item readPacketStream(FriendlyByteBuf stream) {
        return Registry.ITEM.get(new ResourceLocation(stream.readUtf()));
    }

    @Override
    public ResourceLocation key() {
        return REF;
    }
}
