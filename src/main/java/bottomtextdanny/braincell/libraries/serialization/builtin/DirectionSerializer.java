package bottomtextdanny.braincell.libraries.serialization.builtin;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import bottomtextdanny.braincell.libraries.serialization.SimpleDataSerializer;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public final class DirectionSerializer implements DataSerializer<Direction> {
    public static final ResourceLocation REF =
            new ResourceLocation(Braincell.ID, "direction");

    @Override
    public void writeNBT(CompoundTag nbt, Direction obj, String storage) {
        nbt.putByte(storage, (byte)obj.ordinal());
    }

    @Nullable
    @Override
    public Direction readNBT(CompoundTag nbt, String storage) {
        if (!nbt.contains(storage)) return null;
        return Direction.values()[(int) nbt.getByte(storage)];
    }

    @Override
    public void writePacketStream(FriendlyByteBuf stream, Direction obj) {
        stream.writeByte(obj.ordinal());
    }

    @Override
    public Direction readPacketStream(FriendlyByteBuf stream) {
        return Direction.values()[(int) stream.readByte()];
    }

    @Override
    public ResourceLocation key() {
        return REF;
    }
}
