package bottomtextdanny.braincell.libraries._minor.entity_data_manager.serialization;

import bottomtextdanny.braincell.libraries.serialization.EntityDataSerializer;
import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import bottomtextdanny.braincell.libraries.serialization.SimpleDataSerializer;
import bottomtextdanny.braincell.libraries.serialization.WorldDataSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public class Persistent<T> implements _Persistent<T> {
    private final EntityDataReference<T> reference;
    private T objInstance;

    public Persistent(EntityDataReference<T> reference) {
        this.reference = reference;
        this.objInstance = reference.defaultProvider().get();
    }

    public static <E> Persistent<E> of(EntityDataReference<E> reference) {
        return new Persistent<>(reference);
    }

    @Override
    public T get() {
        return this.objInstance;
    }

    @Override
    public void set(T t) {
        this.objInstance = t;
    }

    @Override
    public DataSerializer<T> getSerializer() {
        return this.reference.serializer();
    }

    @Override
    public void writeToPacketStream(FriendlyByteBuf stream, Level level) {
        getSerializer().writePacketStream(stream, this.objInstance);
    }

    @Override
    public T readFromPacketStream(FriendlyByteBuf stream, Level level) {
        set(getSerializer().readPacketStream(stream));

        checkInvalidReadObject();

        return this.objInstance;
    }

    @Override
    public void writeToNBT(CompoundTag nbt, Level level) {
        getSerializer().writeNBT(nbt, this.objInstance, this.reference.storageKey());
    }

    @Override
    public T readFromNBT(CompoundTag nbt, Level level) {
        set(getSerializer().readNBT(nbt, this.reference.storageKey()));

        checkInvalidReadObject();

        return this.objInstance;
    }

    private void checkInvalidReadObject() {
        if (this.objInstance == null) {
            this.objInstance = reference.defaultProvider().get();
        }
    }
}

