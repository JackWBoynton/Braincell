/*
 * Copyright Friday September 23 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries._minor.entity_data_manager.serialization;

import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public class PersistentFloat implements _Persistent<Float> {
	private final EntityDataReference<Float> reference;
	private float floatValue;

	public PersistentFloat(EntityDataReference<Float> reference) {
		this.reference = reference;
		this.floatValue = reference.defaultProvider().get();
	}

	public static PersistentFloat of(EntityDataReference<Float> reference) {
		return new PersistentFloat(reference);
	}

	@Deprecated
	@Override
	public Float get() {
		return floatValue;
	}

	public float getFloat() {
		return floatValue;
	}

	@Deprecated
	@Override
	public void set(Float value) {
		floatValue = value;
	}

	public void setFloat(float value) {
		floatValue = value;
	}

	@Override
	public DataSerializer<Float> getSerializer() {
		return this.reference.serializer();
	}

	@Override
	public void writeToPacketStream(FriendlyByteBuf stream, Level level) {
		getSerializer().writePacketStream(stream, floatValue);
	}

	@Override
	public Float readFromPacketStream(FriendlyByteBuf stream, Level level) {
		Float value = getSerializer().readPacketStream(stream);

		if (value != null)
			set(value);
		else set(reference.defaultProvider().get());

		return floatValue;
	}

	@Override
	public void writeToNBT(CompoundTag nbt, Level level) {
		getSerializer().writeNBT(nbt, floatValue, reference.storageKey());
	}

	@Override
	public Float readFromNBT(CompoundTag nbt, Level level) {
		Float value = getSerializer().readNBT(nbt, this.reference.storageKey());

		if (value != null)
			set(value);
		else set(reference.defaultProvider().get());

		return floatValue;
	}
}
