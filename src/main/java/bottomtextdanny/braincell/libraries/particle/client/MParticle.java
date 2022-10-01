/*
 * Copyright Saturday October 01 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.particle.client;

import bottomtextdanny.braincell.libraries.particle.client.tickers.ParticleAction;
import com.mojang.math.Vector3f;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public interface MParticle {

	void setPos(double x, double y, double z);

	default void setPos(Vec3 vec) {
		setPos(vec.x, vec.y, vec.z);
	}

	default void addPos(double x, double y, double z) {
		Vec3 pos = getPos();
		setPos(pos.x + x, pos.y +  y, pos.z +  z);
	}

	default void addPos(Vec3 vec) {
		Vec3 pos = getPos();
		setPos(pos.x + vec.x, pos.y +  vec.y, pos.z +  vec.z);
	}

	Vec3 getPos();

	Vec3 getPosO();

	void setDelta(double x, double y, double z);

	default void setDelta(Vec3 vec) {
		setDelta(vec.x, vec.y, vec.z);
	}

	default void addDelta(double x, double y, double z) {
		Vec3 delta = getDelta();
		setDelta(delta.x + x, delta.y +  y, delta.z +  z);
	}

	default void addDelta(Vec3 vec) {
		Vec3 delta = getDelta();
		setDelta(delta.x + vec.x, delta.y +  vec.y, delta.z +  vec.z);
	}

	Vec3 getDelta();

	void setXRot(float value);

	default void addXRot(float value)  {
		setXRot(getXRot() + value);
	}

	float getXRot();

	void setYRot(float value);

	default void addYRot(float value)  {
		setYRot(getYRot() + value);
	}

	float getYRot();

	void setZRot(float value);

	default void addZRot(float value)  {
		setZRot(getZRot() + value);
	}

	float getZRot();

	default void setColor(float r, float g, float b) {
		setR(r);
		setG(g);
		setB(b);
	}

	default void setColor(Vector3f color) {
		setR(color.x());
		setG(color.y());
		setB(color.z());
	}

	default void addColor(Vector3f color)  {
		Vector3f curr = getColor();
		setR(curr.x() + color.x());
		setG(curr.y() + color.y());
		setB(curr.z() + color.z());
	}

	default void addColor(float r, float g, float b) {
		Vector3f curr = getColor();
		setR(curr.x() + r);
		setG(curr.y() + g);
		setB(curr.z() + b);
	}

	default Vector3f getColor() {
		return new Vector3f(getR(), getG(), getB());
	}

	void setR(float value);

	default void addR(float value)  {
		setR(getR() + value);
	}

	float getR();

	void setG(float value);

	default void addG(float value)  {
		setG(getG() + value);
	}

	float getG();


	void setB(float value);

	default void addB(float value)  {
		setB(getB() + value);
	}

	float getB();

	void setA(float value);

	default void addA(float value)  {
		setA(getA() + value);
	}

	float getA();

	ParticleAction start();

	ParticleAction ticker();

	ClientLevel level();

	RandomSource random();

	void setLifeTime(int value);

	default void addLifeTime(int value) {
		setLifeTime(getLifeTime() + value);
	}

	int getLifeTime();

	void setTicks(int value);

	default void addTicks(int value) {
		setTicks(getTicks() + value);
	}

	int getTicks();

	boolean onGround();

	void setPhysics(boolean value);

	boolean hasPhysics();

	void setFriction(float value);

	default void addFriction(float value) {
		setFriction(getFriction() + value);
	}

	float getFriction();

	void setSize(float value);

	default void addSize(float value) {
		setSize(getSize() + value);
	}

	float getSize();

	float getDefaultSize();

	void setGlow(int value);

	default void addGlow(int value) {
		setGlow(getGlow() + value);
	}

	int getGlow();

	void setGravity(float value);

	default void addGravity(float value) {
		setGravity(getGravity() + value);
	}

	float getGravity();

	void setCameraFixation(boolean state);

	void remove();
}
