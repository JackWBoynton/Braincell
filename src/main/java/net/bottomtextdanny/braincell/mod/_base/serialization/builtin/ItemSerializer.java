package net.bottomtextdanny.braincell.mod._base.serialization.builtin;

import net.bottomtextdanny.braincell.Braincell;
import net.bottomtextdanny.braincell.mod._base.serialization.SimpleSerializer;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;

public final class ItemSerializer implements SimpleSerializer<Item> {
    public static final ResourceLocation REF =
            new ResourceLocation(Braincell.ID, "item");

    @Override
    public void writeNBT(CompoundTag nbt, Item obj, String storage) {
        nbt.putString(storage, obj.getRegistryName().toString());
    }

    @Nullable
    @Override
    public Item readNBT(CompoundTag nbt, String storage) {
        if (!nbt.contains(storage)) return null;
        return Registry.ITEM.get(new ResourceLocation(nbt.getString(storage)));
    }

    @Override
    public void writePacketStream(FriendlyByteBuf stream, Item obj) {
        stream.writeUtf(obj.getRegistryName().toString());
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
