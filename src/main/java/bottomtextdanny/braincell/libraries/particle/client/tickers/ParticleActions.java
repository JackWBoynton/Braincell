/*
 * Copyright Saturday October 01 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.particle.client.tickers;

import bottomtextdanny.braincell.libraries.particle.client.MParticle;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public final class ParticleActions {

	public static ParticleAction rotateXZByMovement() {
		return particle -> {
			Vec3 delta = particle.getPos().subtract(particle.getPosO());

			float y = (float) delta.y * 50;
			particle.addXRot((float) delta.z * 120 + y);
			particle.addZRot((float) delta.x * 120 + y);
		};
	}

	public static ParticleAction addDelta(double x, double y, double z) {
		return particle -> particle.addDelta(x, y, z);
	}

	public static ParticleAction addDelta(@Nullable ParticleDouble xF, @Nullable ParticleDouble yF, @Nullable ParticleDouble zF) {
		return particle -> particle.addDelta(xF == null ? 0.0 : xF.compute(particle), yF == null ? 0.0 : yF.compute(particle), zF == null ? 0.0 : zF.compute(particle));
	}

	public static ParticleAction setDelta(double x, double y, double z) {
		return particle -> particle.setDelta(x, y, z);
	}

	public static ParticleAction setDelta(@Nullable ParticleDouble xF, @Nullable ParticleDouble yF, @Nullable ParticleDouble zF) {
		return particle -> particle.setDelta(xF == null ? 0.0 : xF.compute(particle), yF == null ? 0.0 : yF.compute(particle), zF == null ? 0.0 : zF.compute(particle));
	}

	public static ParticleAction deltaMultiplier(double x, double y, double z) {
		return particle -> particle.setDelta(particle.getDelta().multiply(x, y, z));
	}

	public static ParticleAction deltaScale(double value) {
		return particle -> particle.setDelta(particle.getDelta().scale(value));
	}

	public static ParticleAction addGravity(float value) {
		return particle -> particle.addGravity(value);
	}

	public static ParticleAction addGravity(ParticleDouble func) {
		return particle -> particle.addGravity((float) func.compute(particle));
	}

	public static ParticleAction setGravity(float value) {
		return particle -> particle.setGravity(value);
	}

	public static ParticleAction setGravity(ParticleDouble func) {
		return particle -> particle.setGravity((float) func.compute(particle));
	}

	public static ParticleAction addFriction(float value) {
		return particle -> particle.addFriction(value);
	}

	public static ParticleAction addFriction(ParticleDouble func) {
		return particle -> particle.addFriction((float) func.compute(particle));
	}

	public static ParticleAction setFriction(float value) {
		return particle -> particle.setFriction(value);
	}

	public static ParticleAction setFriction(ParticleDouble func) {
		return particle -> particle.setFriction((float) func.compute(particle));
	}

	public static ParticleAction addSize(float value) {
		return particle -> particle.addSize(value);
	}

	public static ParticleAction addSize(ParticleDouble func) {
		return particle -> particle.addSize((float) func.compute(particle));
	}

	public static ParticleAction setSize(float value) {
		return particle -> particle.setSize(value);
	}

	public static ParticleAction setSize(ParticleDouble func) {
		return particle -> particle.setSize((float) func.compute(particle));
	}

	public static ParticleAction addGlow(int value) {
		return particle -> particle.addGlow(value);
	}

	public static ParticleAction addGlow(ParticleDouble func) {
		return particle -> particle.addGlow((int) func.compute(particle));
	}

	public static ParticleAction setGlow(int value) {
		return particle -> particle.setGlow(value);
	}

	public static ParticleAction setGlow(ParticleDouble func) {
		return particle -> particle.setGlow((int) func.compute(particle));
	}

	public static ParticleAction addColor(int colorCode) {
		return particle -> {
			particle.setR(particle.getR() + (colorCode >> 16 & 255) / 255.0F);
			particle.setG(particle.getG() + (colorCode >> 8 & 255) / 255.0F);
			particle.setB(particle.getB() + (colorCode & 255) / 255.0F);
		};
	}

	public static ParticleAction setColor(int colorCode) {
		return particle -> {
			particle.setR((colorCode >> 16 & 255) / 255.0F);
			particle.setG((colorCode >> 8 & 255) / 255.0F);
			particle.setB((colorCode & 255) / 255.0F);
		};
	}

	public static ParticleAction addColor(float r, float g, float b) {
		return particle -> {
			particle.setR(particle.getR() + r);
			particle.setG(particle.getG() + g);
			particle.setB(particle.getB() + b);
		};
	}

	public static ParticleAction setColor(float r, float g, float b) {
		return particle -> {
			particle.setR(r);
			particle.setG(g);
			particle.setB(b);
		};
	}

	public static ParticleAction addR(float value) {
		return particle -> particle.addR(value);
	}

	public static ParticleAction addR(ParticleDouble func) {
		return particle -> particle.addR((float) func.compute(particle));
	}

	public static ParticleAction setR(float value) {
		return particle -> particle.setR(value);
	}

	public static ParticleAction setR(ParticleDouble func) {
		return particle -> particle.setR((float) func.compute(particle));
	}

	public static ParticleAction addG(float value) {
		return particle -> particle.addG(value);
	}

	public static ParticleAction addG(ParticleDouble func) {
		return particle -> particle.addG((float) func.compute(particle));
	}

	public static ParticleAction setG(float value) {
		return particle -> particle.setG(value);
	}

	public static ParticleAction setG(ParticleDouble func) {
		return particle -> particle.setG((float) func.compute(particle));
	}

	public static ParticleAction addB(float value) {
		return particle -> particle.addB(value);
	}

	public static ParticleAction addB(ParticleDouble func) {
		return particle -> particle.addB((float) func.compute(particle));
	}

	public static ParticleAction setB(float value) {
		return particle -> particle.setB(value);
	}

	public static ParticleAction setB(ParticleDouble func) {
		return particle -> particle.setB((float) func.compute(particle));
	}

	public static ParticleAction addA(float value) {
		return particle -> particle.addA(value);
	}

	public static ParticleAction addA(ParticleDouble func) {
		return particle -> particle.addA((float) func.compute(particle));
	}

	public static ParticleAction setA(float value) {
		return particle -> particle.setA(value);
	}

	public static ParticleAction setA(ParticleDouble func) {
		return particle -> particle.setA((float) func.compute(particle));
	}

	public static ParticleAction addXRot(float value) {
		return particle -> particle.addXRot(value);
	}

	public static ParticleAction addXRot(ParticleDouble func) {
		return particle -> particle.addXRot((float) func.compute(particle));
	}

	public static ParticleAction setXRot(float value) {
		return particle -> particle.setXRot(value);
	}

	public static ParticleAction setXRot(ParticleDouble func) {
		return particle -> particle.setXRot((float) func.compute(particle));
	}

	public static ParticleAction addYRot(float value) {
		return particle -> particle.addYRot(value);
	}

	public static ParticleAction addYRot(ParticleDouble func) {
		return particle -> particle.addYRot((float) func.compute(particle));
	}

	public static ParticleAction setYRot(float value) {
		return particle -> particle.setYRot(value);
	}

	public static ParticleAction setYRot(ParticleDouble func) {
		return particle -> particle.setYRot((float) func.compute(particle));
	}

	public static ParticleAction addZRot(float value) {
		return particle -> particle.addZRot(value);
	}

	public static ParticleAction addZRot(ParticleDouble func) {
		return particle -> particle.addZRot((float) func.compute(particle));
	}

	public static ParticleAction setZRot(float value) {
		return particle -> particle.setZRot(value);
	}

	public static ParticleAction setZRot(ParticleDouble func) {
		return particle -> particle.setZRot((float) func.compute(particle));
	}

	public static ParticleAction addLifeTime(int value) {
		return particle -> particle.addLifeTime(value);
	}

	public static ParticleAction addLifeTime(ParticleDouble func) {
		return particle -> particle.addLifeTime((int) func.compute(particle));
	}

	public static ParticleAction setLifeTime(int value) {
		return particle -> particle.setLifeTime(value);
	}

	public static ParticleAction setLifeTime(ParticleDouble func) {
		return particle -> particle.setLifeTime((int) func.compute(particle));
	}

	public static ParticleAction addTicks(int value) {
		return particle -> particle.addTicks(value);
	}

	public static ParticleAction addTicks(ParticleDouble func) {
		return particle -> particle.addTicks((int) func.compute(particle));
	}

	public static ParticleAction setTicks(int value) {
		return particle -> particle.setTicks(value);
	}

	public static ParticleAction setTicks(ParticleDouble func) {
		return particle -> particle.setTicks((int) func.compute(particle));
	}

	public static ParticleAction onlyIf(ParticleBoolean conditional, ParticleAction action) {
		return particle -> {
			if (conditional.compute(particle))
				action.execute(particle);
		};
	}

	public static ParticleAction whether(ParticleBoolean conditional, ParticleAction actionp, ParticleAction actionn) {
		return particle -> {
			if (conditional.compute(particle))
				actionp.execute(particle);
			else actionn.execute(particle);
		};
	}

	public static ParticleAction kill() {
		return MParticle::remove;
	}
}
