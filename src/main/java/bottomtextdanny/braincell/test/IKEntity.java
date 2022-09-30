/*
 * Copyright Friday August 05 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.test;

import bottomtextdanny.braincell.libraries.model_animation.ik.CCDIKPartData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class IKEntity extends Entity {
	public CCDIKPartData bottom = new CCDIKPartData();
	public CCDIKPartData middle = new CCDIKPartData();
	public CCDIKPartData middleTop = new CCDIKPartData();
	public CCDIKPartData top = new CCDIKPartData();
	//n
	public CCDIKPartData tip = new CCDIKPartData();

	public IKEntity(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData() {

	}

	@Override
	protected void readAdditionalSaveData(CompoundTag p_20052_) {

	}

	@Override
	protected void addAdditionalSaveData(CompoundTag p_20139_) {

	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
