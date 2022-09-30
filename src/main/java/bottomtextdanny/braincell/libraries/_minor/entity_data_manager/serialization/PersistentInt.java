/*
 * Copyright Friday September 23 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries._minor.entity_data_manager.serialization;

import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public class PersistentInt implements _Persistent<Integer> {
	private final EntityDataReference<Integer> reference;
	private int intValue;

	public PersistentInt(EntityDataReference<Integer> reference) {
		this.reference = reference;
		this.intValue = reference.defaultProvider().get();
	}

	public static PersistentInt of(EntityDataReference<Integer> reference) {
		return new PersistentInt(reference);
	}

	@Deprecated
	@Override
	public Integer get() {
		return intValue;
	}

	public int getInt() {
		return intValue;
	}

	@Deprecated
	@Override
	public void set(Integer value) {
		intValue = value;
	}

	public void setInt(int value) {
		intValue = value;
	}

	@Override
	public DataSerializer<Integer> getSerializer() {
		return this.reference.serializer();
	}

	@Override
	public void writeToPacketStream(FriendlyByteBuf stream, Level level) {
		getSerializer().writePacketStream(stream, intValue);
	}

	@Override
	public Integer readFromPacketStream(FriendlyByteBuf stream, Level level) {
		Integer value = getSerializer().readPacketStream(stream);

		if (value != null)
			set(value);
		else set(reference.defaultProvider().get());

		return intValue;
	}

	@Override
	public void writeToNBT(CompoundTag nbt, Level level) {
		getSerializer().writeNBT(nbt, intValue, reference.storageKey());
	}

	@Override
	public Integer readFromNBT(CompoundTag nbt, Level level) {
		Integer value = getSerializer().readNBT(nbt, this.reference.storageKey());

		if (value != null)
			set(value);
		else set(reference.defaultProvider().get());

		return intValue;
	}
}
