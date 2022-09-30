/*
 * Copyright Friday September 23 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries._minor.entity_data_manager.serialization;

import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public class PersistentByte implements _Persistent<Byte> {
	private final EntityDataReference<Byte> reference;
	private byte byteValue;

	public PersistentByte(EntityDataReference<Byte> reference) {
		this.reference = reference;
		this.byteValue = reference.defaultProvider().get();
	}

	public static PersistentByte of(EntityDataReference<Byte> reference) {
		return new PersistentByte(reference);
	}

	@Deprecated
	@Override
	public Byte get() {
		return byteValue;
	}

	public byte getByte() {
		return byteValue;
	}

	@Deprecated
	@Override
	public void set(Byte value) {
		byteValue = value;
	}

	public void setByte(byte value) {
		byteValue = value;
	}

	@Override
	public DataSerializer<Byte> getSerializer() {
		return this.reference.serializer();
	}

	@Override
	public void writeToPacketStream(FriendlyByteBuf stream, Level level) {
		getSerializer().writePacketStream(stream, byteValue);
	}

	@Override
	public Byte readFromPacketStream(FriendlyByteBuf stream, Level level) {
		Byte value = getSerializer().readPacketStream(stream);

		if (value != null)
			set(value);
		else set(reference.defaultProvider().get());

		return byteValue;
	}

	@Override
	public void writeToNBT(CompoundTag nbt, Level level) {
		getSerializer().writeNBT(nbt, byteValue, reference.storageKey());
	}

	@Override
	public Byte readFromNBT(CompoundTag nbt, Level level) {
		Byte value = getSerializer().readNBT(nbt, this.reference.storageKey());

		if (value != null)
			set(value);
		else set(reference.defaultProvider().get());

		return byteValue;
	}
}
