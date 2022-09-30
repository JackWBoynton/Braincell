package bottomtextdanny.braincell.libraries.serialization.builtin;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.base.scheduler.IntScheduler;
import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import bottomtextdanny.braincell.libraries.serialization.SimpleDataSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public final class IntSchedulerSerializer implements DataSerializer<IntScheduler.Simple> {
    public static final ResourceLocation REF =
            new ResourceLocation(Braincell.ID, "simple_int_scheduler");

    @Override
    public void writeNBT(CompoundTag nbt, IntScheduler.Simple obj, String storage) {
        nbt.putInt(storage + "_bound", obj.bound());
        nbt.putInt(storage, obj.current());
    }

    @Nullable
    @Override
    public IntScheduler.Simple readNBT(CompoundTag nbt, String storage) {
        if (!nbt.contains(storage)) return null;
        IntScheduler.Simple simple = new IntScheduler.Simple(nbt.getInt(storage));
        simple.hackyCounterSet(nbt.getInt(storage + "_bound"));
        return simple;
    }

    @Override
    public void writePacketStream(FriendlyByteBuf stream, IntScheduler.Simple obj) {
        stream.writeVarInt(obj.bound());
        stream.writeVarInt(obj.current());
    }

    @Override
    public IntScheduler.Simple readPacketStream(FriendlyByteBuf stream) {
        int cur = stream.readVarInt();
        IntScheduler.Simple simple = new IntScheduler.Simple(stream.readVarInt());
        simple.hackyCounterSet(cur);
        return simple;
    }

    @Override
    public ResourceLocation key() {
        return REF;
    }
}
