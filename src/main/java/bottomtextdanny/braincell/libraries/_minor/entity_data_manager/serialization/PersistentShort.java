/*
 * Copyright Friday September 23 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries._minor.entity_data_manager.serialization;

import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public class PersistentShort implements _Persistent<Short> {
	private final EntityDataReference<Short> reference;
	private short shortValue;

	public PersistentShort(EntityDataReference<Short> reference) {
		this.reference = reference;
		this.shortValue = reference.defaultProvider().get();
	}

	public static PersistentShort of(EntityDataReference<Short> reference) {
		return new PersistentShort(reference);
	}

	@Deprecated
	@Override
	public Short get() {
		return shortValue;
	}
	
	public short getShort() {
		return shortValue;
	}

	@Deprecated
	@Override
	public void set(Short value) {
		shortValue = value;
	}
	
	public void setShort(short value) {
		shortValue = value;
	}

	@Override
	public DataSerializer<Short> getSerializer() {
		return this.reference.serializer();
	}

	@Override
	public void writeToPacketStream(FriendlyByteBuf stream, Level level) {
		getSerializer().writePacketStream(stream, shortValue);
	}

	@Override
	public Short readFromPacketStream(FriendlyByteBuf stream, Level level) {
		Short value = getSerializer().readPacketStream(stream);

		if (value != null)
			set(value);
		else set(reference.defaultProvider().get());

		return shortValue;
	}

	@Override
	public void writeToNBT(CompoundTag nbt, Level level) {
		getSerializer().writeNBT(nbt, shortValue, reference.storageKey());
	}

	@Override
	public Short readFromNBT(CompoundTag nbt, Level level) {
		Short value = getSerializer().readNBT(nbt, this.reference.storageKey());

		if (value != null)
			set(value);
		else set(reference.defaultProvider().get());

		return shortValue;
	}
}
