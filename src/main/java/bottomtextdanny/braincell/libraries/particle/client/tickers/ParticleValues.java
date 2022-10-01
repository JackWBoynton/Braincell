/*
 * Copyright Saturday October 01 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.particle.client.tickers;

import bottomtextdanny.braincell.libraries.particle.client.MParticle;
import net.minecraft.util.Mth;

public final class ParticleValues {

	public static ParticleDouble constant(double value) {
		return particle -> value;
	}

	public static ParticleDouble sin(ParticleDouble value) {
		return particle -> Mth.sin((float) value.compute(particle));
	}

	public static ParticleDouble cos(ParticleDouble value) {
		return particle -> Mth.cos((float) value.compute(particle));
	}

	public static ParticleDouble size() {
		return MParticle::getSize;
	}

	public static ParticleDouble defaultSize() {
		return MParticle::getDefaultSize;
	}

	public static ParticleDouble lifeTime() {
		return MParticle::getLifeTime;
	}

	public static ParticleDouble ticks() {
		return MParticle::getTicks;
	}

	public static ParticleDouble prog() {
		return particle -> particle.getTicks() / (float)particle.getLifeTime();
	}

	public static ParticleDouble invProg() {
		return particle -> 1.0F - particle.getTicks() / (float)particle.getLifeTime();
	}

	public static ParticleDouble inv(ParticleDouble func, double root) {
		return particle -> root - func.compute(particle);
	}

	public static ParticleDouble r() {
		return MParticle::getR;
	}

	public static ParticleDouble g() {
		return MParticle::getG;
	}

	public static ParticleDouble b() {
		return MParticle::getB;
	}

	public static ParticleDouble a() {
		return MParticle::getA;
	}

	public static ParticleDouble xRot() {
		return MParticle::getXRot;
	}

	public static ParticleDouble yRot() {
		return MParticle::getYRot;
	}

	public static ParticleDouble zRot() {
		return MParticle::getZRot;
	}

	public static ParticleDouble gravity() {
		return MParticle::getGravity;
	}

	public static ParticleBoolean onGround() {
		return MParticle::onGround;
	}
}
