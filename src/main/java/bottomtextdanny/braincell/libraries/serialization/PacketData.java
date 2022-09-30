package bottomtextdanny.braincell.libraries.serialization;

import net.minecraft.network.FriendlyByteBuf;

public final class PacketData<T> {
    public final DataSerializer<T> serializer;
    private T objInstance;

    public PacketData(DataSerializer<T> ser, T obj) {
        this.serializer = ser;
        this.objInstance = obj;
    }

    public T get() {
        return this.objInstance;
    }

    public void set(T t) {
        this.objInstance = t;
    }

    public static <E> PacketData<E> of(DataSerializer<E> ser, E obj) {
        return new PacketData<>(ser, obj);
    }

    public void writeToStream(FriendlyByteBuf stream) {
        serializer.writePacketStream(stream, this.objInstance);
    }

    public DataSerializer<T> getSerializer() {
        return this.serializer;
    }
}
