/*
 * Copyright Friday September 23 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries._minor.entity_data_manager.serialization;

import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public interface _Persistent<T> {

	T get();

	void set(T t);

	DataSerializer<T> getSerializer();

	void writeToPacketStream(FriendlyByteBuf stream, Level level);

	T readFromPacketStream(FriendlyByteBuf stream, Level level);

	void writeToNBT(CompoundTag nbt, Level level);

	T readFromNBT(CompoundTag nbt, Level level);
}
