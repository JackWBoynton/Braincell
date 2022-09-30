package bottomtextdanny.braincell.libraries.serialization.builtin;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import bottomtextdanny.braincell.libraries.serialization.SimpleDataSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public final class ItemStackSerializer implements DataSerializer<ItemStack> {
    public static final ResourceLocation REF =
            new ResourceLocation(Braincell.ID, "item_stack");

    @Override
    public void writeNBT(CompoundTag nbt, ItemStack obj, String storage) {
        nbt.put(storage, obj.serializeNBT());
    }

    @Nullable
    @Override
    public ItemStack readNBT(CompoundTag nbt, String storage) {
        if (!nbt.contains(storage)) return null;
        return ItemStack.of(nbt.getCompound(storage));
    }

    @Override
    public void writePacketStream(FriendlyByteBuf stream, ItemStack obj) {
        stream.writeItem(obj);
    }

    @Override
    public ItemStack readPacketStream(FriendlyByteBuf stream) {
        return stream.readItem();
    }

    @Override
    public ResourceLocation key() {
        return REF;
    }
}
