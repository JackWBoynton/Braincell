/*
 * Copyright Friday September 23 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries._minor.entity_data_manager.serialization;

import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public class PersistentBoolean implements _Persistent<Boolean> {
	private final EntityDataReference<Boolean> reference;
	private boolean booleanValue;

	public PersistentBoolean(EntityDataReference<Boolean> reference) {
		this.reference = reference;
		this.booleanValue = reference.defaultProvider().get();
	}

	public static PersistentBoolean of(EntityDataReference<Boolean> reference) {
		return new PersistentBoolean(reference);
	}

	@Deprecated
	@Override
	public Boolean get() {
		return booleanValue;
	}
	
	public boolean getBoolean() {
		return booleanValue;
	}

	@Deprecated
	@Override
	public void set(Boolean value) {
		booleanValue = value;
	}
	
	public void setBoolean(boolean value) {
		booleanValue = value;
	}

	@Override
	public DataSerializer<Boolean> getSerializer() {
		return this.reference.serializer();
	}

	@Override
	public void writeToPacketStream(FriendlyByteBuf stream, Level level) {
		getSerializer().writePacketStream(stream, booleanValue);
	}

	@Override
	public Boolean readFromPacketStream(FriendlyByteBuf stream, Level level) {
		Boolean value = getSerializer().readPacketStream(stream);

		if (value != null)
			set(value);
		else set(reference.defaultProvider().get());

		return booleanValue;
	}

	@Override
	public void writeToNBT(CompoundTag nbt, Level level) {
		getSerializer().writeNBT(nbt, booleanValue, reference.storageKey());
	}

	@Override
	public Boolean readFromNBT(CompoundTag nbt, Level level) {
		Boolean value = getSerializer().readNBT(nbt, this.reference.storageKey());

		if (value != null)
			set(value);
		else set(reference.defaultProvider().get());

		return booleanValue;
	}
}
