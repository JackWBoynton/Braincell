/*
 * Copyright Friday September 23 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries._minor.entity_data_manager.serialization;

import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.UUID;

public class PersistentEntityReference implements _Persistent<UUID> {
	private final EntityDataReference<UUID> reference;
	public final int searchRate;
	private UUID uuid;
	@Nullable
	private Entity entity;

	public PersistentEntityReference(int searchRate, EntityDataReference<UUID> reference) {
		this.reference = reference;
		this.uuid = reference.defaultProvider().get();
		this.searchRate = searchRate;
	}

	public static PersistentEntityReference of(int searchRate, EntityDataReference<UUID> reference) {
		return new PersistentEntityReference(searchRate, reference);
	}

	public static PersistentEntityReference of(EntityDataReference<UUID> reference) {
		return new PersistentEntityReference(5, reference);
	}

	@Deprecated
	@Override
	public UUID get() {
		return uuid;
	}
	
	public Entity getContained() {
		return entity;
	}

	@Nullable
	public Entity getOrSearch(Level level) {
		if (entity != null) return entity;
		if (uuid == null) return null;
		if (level instanceof ServerLevel && level.getGameTime() % searchRate == 0) {
			Entity entity = ((ServerLevel) level).getEntity(uuid);
			this.entity = entity;
			return entity;
		}
		return null;
	}

	@Deprecated
	@Override
	public void set(UUID value) {
		uuid = value;
		entity = null;
	}

	public void set(Entity entity) {
		if (entity.isAddedToWorld()) {
			this.entity = entity;
			this.uuid = entity.getUUID();
		}
	}

	@Override
	public DataSerializer<UUID> getSerializer() {
		return this.reference.serializer();
	}

	@Override
	public void writeToPacketStream(FriendlyByteBuf stream, Level level) {
		getSerializer().writePacketStream(stream, uuid);
	}

	@Override
	public UUID readFromPacketStream(FriendlyByteBuf stream, Level level) {
		set(getSerializer().readPacketStream(stream));

		checkInvalidReadObject();

		return uuid;
	}

	@Override
	public void writeToNBT(CompoundTag nbt, Level level) {
		getSerializer().writeNBT(nbt, uuid, reference.storageKey());
	}

	@Override
	public UUID readFromNBT(CompoundTag nbt, Level level) {
		set(getSerializer().readNBT(nbt, this.reference.storageKey()));

		checkInvalidReadObject();

		return uuid;
	}

	private void checkInvalidReadObject() {
		if (this.uuid == null) {
			this.uuid = reference.defaultProvider().get();
		}
	}
}
