/*
 * Copyright Saturday October 01 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.particle.client;

import bottomtextdanny.braincell.libraries.particle.ModularParticleType;
import bottomtextdanny.braincell.libraries.particle.client.tickers.ParticleAction;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public abstract class ModularParticle extends Particle implements MParticle {
	protected final ModularParticleType type;
	protected final ParticleAction<?> ticker;
	protected final ParticleAction<?> start;
	public float xRotO;
	public float yRotO;
	public float zRotO;
	public float xRot;
	public float yRot;
	public float zRot;
	public float sizeO;
	public float size;
	public byte flags;

	protected ModularParticle(ModularParticleType type, ClientLevel world, double x, double y, double z, ParticleAction start, ParticleAction ticker) {
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);
		this.type = type;
		this.ticker = ticker;
		this.start = start;
		init(0.0D, 0.0D, 0.0D);
	}

	public ModularParticle(ModularParticleType type, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ParticleAction start, ParticleAction ticker) {
		super(world, x, y, z, xSpeed, ySpeed, zSpeed);
		this.type = type;
		this.ticker = ticker;
		this.start = start;
		init(xSpeed, ySpeed, zSpeed);
	}

	protected void init(double xDelta, double yDelta, double zDelta) {
		xRotO = 0.0F;
		yRotO = 0.0F;
		zRotO = 0.0F;
		xRot = 0.0F;
		yRot = 0.0F;
		zRot = 0.0F;
		sizeO = 0.0F;
		size = 0.0F;
		rCol = 1.0F;
		gCol = 1.0F;
		bCol = 1.0F;
		xd = xDelta;
		yd = yDelta;
		zd = zDelta;
		start._execute(this);
		customInit();
	}

	public abstract void customInit();

	@Override
	public void tick() {
		xRotO = xRot;
		yRotO = yRot;
		zRotO = zRot;
		sizeO = size;

		super.tick();

		ticker._execute(this);
	}

	@Override
	public void move(double xDelta, double yDelta, double zDelta) {
		if (getFlag(IGNORE_COLLISION)) setPos(getPos().add(xDelta, yDelta, zDelta));
		else super.move(xDelta, yDelta, zDelta);
	}

	@Override
	protected int getLightColor(float tickOffset) {
		return getFlag(GLOW) ? 15728880 : super.getLightColor(tickOffset);
	}

	@Override
	public ParticleRenderType getRenderType() {
		return getFlag(TRANSLUCENT) ? ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT : ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public boolean shouldCull() {
		return !getFlag(IGNORE_CULLING);
	}

	@Override
	public Vec3 getPos() {
		return new Vec3(x, y, z);
	}

	@Override
	public Vec3 getPosO() {
		return new Vec3(xo, yo, zo);
	}

	@Override
	public void setDelta(double x, double y, double z) {
		xd = x;
		yd = y;
		zd = z;
	}

	@Override
	public Vec3 getDelta() {
		return new Vec3(xd, yd, zd);
	}

	@Override
	public void setXRot(float value) {
		xRot = value;
	}

	@Override
	public float getXRot() {
		return xRot;
	}

	@Override
	public void setYRot(float value) {
		yRot = value;
	}

	@Override
	public float getYRot() {
		return yRot;
	}

	@Override
	public void setZRot(float value) {
		zRot = value;
	}

	@Override
	public float getZRot() {
		return zRot;
	}

	@Override
	public void setR(float value) {
		rCol = value;
	}

	@Override
	public float getR() {
		return rCol;
	}

	@Override
	public void setG(float value) {
		gCol = value;
	}

	@Override
	public float getG() {
		return gCol;
	}

	@Override
	public void setB(float value) {
		bCol = value;
	}

	@Override
	public float getB() {
		return bCol;
	}

	@Override
	public void setA(float value) {
		alpha = value;
	}

	@Override
	public float getA() {
		return alpha;
	}

	@Override
	public ParticleAction start() {
		return start;
	}

	@Override
	public ParticleAction ticker() {
		return ticker;
	}

	@Override
	public ClientLevel level() {
		return level;
	}

	@Override
	public RandomSource random() {
		return random;
	}

	@Override
	public void setLifeTime(int value) {
		lifetime = value;
	}

	@Override
	public int getLifeTime() {
		return lifetime;
	}

	@Override
	public void setTicks(int value) {
		age = value;
	}

	@Override
	public int getTicks() {
		return age;
	}

	@Override
	public boolean onGround() {
		return onGround;
	}

	@Override
	public void setPhysics(boolean value) {
		hasPhysics = value;
	}

	@Override
	public boolean hasPhysics() {
		return hasPhysics;
	}

	@Override
	public void setFriction(float value) {
		friction = value;
	}

	@Override
	public float getFriction() {
		return friction;
	}

	@Override
	public void setSize(float value) {
		size = value;
	}

	@Override
	public float getSize() {
		return size;
	}

	@Override
	public void setFlags(byte value) {
		flags = value;
	}

	@Override
	public byte getFlags() {
		return flags;
	}

	@Override
	public void setGravity(float value) {
		gravity = value;
	}

	@Override
	public float getGravity() {
		return gravity;
	}
}
