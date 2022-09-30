package bottomtextdanny.braincell.libraries.serialization.builtin;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.base.scheduler.IntScheduler;
import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import bottomtextdanny.braincell.libraries.serialization.SimpleDataSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public final class RangedIntSchedulerSerializer implements DataSerializer<IntScheduler.Ranged> {
    public static final ResourceLocation REF =
            new ResourceLocation(Braincell.ID, "ranged_int_scheduler");

    @Override
    public void writeNBT(CompoundTag nbt, IntScheduler.Ranged obj, String storage) {
        nbt.putInt(storage + "_min", obj.minBound);
        nbt.putInt(storage + "_max", obj.maxBound);
        nbt.putInt(storage + "_bound", obj.bound());
        nbt.putInt(storage, obj.current());
    }

    @Nullable
    @Override
    public IntScheduler.Ranged readNBT(CompoundTag nbt, String storage) {
        if (!nbt.contains(storage)) return null;
        IntScheduler.Ranged simple = new IntScheduler.Ranged(nbt.getInt(storage + "_min"),
                nbt.getInt(storage + "_max"), nbt.getInt(storage + "_bound"));
        simple.hackyCounterSet(nbt.getInt(storage));
        return simple;
    }

    @Override
    public void writePacketStream(FriendlyByteBuf stream, IntScheduler.Ranged obj) {
        stream.writeVarInt(obj.minBound);
        stream.writeVarInt(obj.maxBound);
        stream.writeVarInt(obj.bound());
        stream.writeVarInt(obj.current());
    }

    @Override
    public IntScheduler.Ranged readPacketStream(FriendlyByteBuf stream) {
        IntScheduler.Ranged ranged = new IntScheduler.Ranged(stream.readVarInt(), stream.readVarInt(), stream.readVarInt());
        ranged.hackyCounterSet(stream.readVarInt());
        return ranged;
    }

    @Override
    public ResourceLocation key() {
        return REF;
    }
}
