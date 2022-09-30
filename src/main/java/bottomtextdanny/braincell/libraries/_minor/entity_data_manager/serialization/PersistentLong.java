/*
 * Copyright Friday September 23 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries._minor.entity_data_manager.serialization;

import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public class PersistentLong implements _Persistent<Long> {
	private final EntityDataReference<Long> reference;
	private long longValue;

	public PersistentLong(EntityDataReference<Long> reference) {
		this.reference = reference;
		this.longValue = reference.defaultProvider().get();
	}

	public static PersistentLong of(EntityDataReference<Long> reference) {
		return new PersistentLong(reference);
	}

	@Deprecated
	@Override
	public Long get() {
		return longValue;
	}
	
	public long getLong() {
		return longValue;
	}

	@Deprecated
	@Override
	public void set(Long value) {
		longValue = value;
	}
	
	public void setLong(long value) {
		longValue = value;
	}

	@Override
	public DataSerializer<Long> getSerializer() {
		return this.reference.serializer();
	}

	@Override
	public void writeToPacketStream(FriendlyByteBuf stream, Level level) {
		getSerializer().writePacketStream(stream, longValue);
	}

	@Override
	public Long readFromPacketStream(FriendlyByteBuf stream, Level level) {
		Long value = getSerializer().readPacketStream(stream);

		if (value != null)
			set(value);
		else set(reference.defaultProvider().get());

		return longValue;
	}

	@Override
	public void writeToNBT(CompoundTag nbt, Level level) {
		getSerializer().writeNBT(nbt, longValue, reference.storageKey());
	}

	@Override
	public Long readFromNBT(CompoundTag nbt, Level level) {
		Long value = getSerializer().readNBT(nbt, this.reference.storageKey());

		if (value != null)
			set(value);
		else set(reference.defaultProvider().get());

		return longValue;
	}
}
