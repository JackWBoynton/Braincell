/*
 * Copyright Friday September 23 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries._minor.entity_data_manager.serialization;

import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public class PersistentDouble implements _Persistent<Double> {
	private final EntityDataReference<Double> reference;
	private double doubleValue;

	public PersistentDouble(EntityDataReference<Double> reference) {
		this.reference = reference;
		this.doubleValue = reference.defaultProvider().get();
	}

	public static PersistentDouble of(EntityDataReference<Double> reference) {
		return new PersistentDouble(reference);
	}

	@Deprecated
	@Override
	public Double get() {
		return doubleValue;
	}
	
	public double getDouble() {
		return doubleValue;
	}

	@Deprecated
	@Override
	public void set(Double value) {
		doubleValue = value;
	}
	
	public void setDouble(double value) {
		doubleValue = value;
	}

	@Override
	public DataSerializer<Double> getSerializer() {
		return this.reference.serializer();
	}

	@Override
	public void writeToPacketStream(FriendlyByteBuf stream, Level level) {
		getSerializer().writePacketStream(stream, doubleValue);
	}

	@Override
	public Double readFromPacketStream(FriendlyByteBuf stream, Level level) {
		Double value = getSerializer().readPacketStream(stream);

		if (value != null)
			set(value);
		else set(reference.defaultProvider().get());

		return doubleValue;
	}

	@Override
	public void writeToNBT(CompoundTag nbt, Level level) {
		getSerializer().writeNBT(nbt, doubleValue, reference.storageKey());
	}

	@Override
	public Double readFromNBT(CompoundTag nbt, Level level) {
		Double value = getSerializer().readNBT(nbt, this.reference.storageKey());

		if (value != null)
			set(value);
		else set(reference.defaultProvider().get());

		return doubleValue;
	}
}
