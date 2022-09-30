/*
 * Copyright Sunday September 18 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries._minor.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityReference {
	public static final UUID DEF = UUID.fromString("00000000-0000-0000-0000-000000000000");
	@Nullable
	private Entity entity;
	@Nullable
	private UUID uuid;
	public final int searchRate;

	public EntityReference() {
		super();
		this.searchRate = 0;
	}

	public EntityReference(int searchRate) {
		super();
		this.searchRate = searchRate;
	}

	public EntityReference(int searchRate, UUID uuid) {
		super();
		this.searchRate = searchRate;
		this.uuid = uuid;
	}

	public void set(UUID uuid) {
		this.uuid = uuid;
		entity = null;
	}

	public void set(Entity entity) {
		if (entity.isAddedToWorld()) {
			this.entity = entity;
			this.uuid = entity.getUUID();
		}
	}

	@Nullable
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

	@Nullable
	public UUID getUuid() {
		return uuid;
	}
}
