/*
 * Copyright Saturday October 01 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.particle.client.tickers;

import bottomtextdanny.braincell.libraries.particle.client.MParticle;

public interface ParticleDouble {

	double compute(MParticle particle);

	default ParticleDouble add(ParticleDouble other) {
		return particle -> compute(particle) + other.compute(particle);
	}

	default ParticleDouble add(double value) {
		return particle -> compute(particle) + value;
	}

	default ParticleDouble sub(ParticleDouble other) {
		return particle -> compute(particle) - other.compute(particle);
	}

	default ParticleDouble sub(double value) {
		return particle -> compute(particle) - value;
	}

	default ParticleDouble scale(ParticleDouble other) {
		return particle -> compute(particle) * other.compute(particle);
	}

	default ParticleDouble scale(double value) {
		return particle -> compute(particle) * value;
	}

	default ParticleDouble div(ParticleDouble other) {
		return particle -> compute(particle) / other.compute(particle);
	}

	default ParticleDouble div(double value) {
		return particle -> compute(particle) / value;
	}

	default ParticleDouble min(ParticleDouble other) {
		return particle -> Math.min(compute(particle), other.compute(particle));
	}

	default ParticleDouble min(double value) {
		return particle -> Math.min(compute(particle), value);
	}

	default ParticleDouble max(ParticleDouble other) {
		return particle -> Math.max(compute(particle), other.compute(particle));
	}

	default ParticleDouble max(double value) {
		return particle -> Math.max(compute(particle), value);
	}

	default ParticleBoolean below(ParticleDouble threshold) {
		return particle -> compute(particle) < threshold.compute(particle);
	}

	default ParticleBoolean below(double value) {
		return particle -> compute(particle) < value;
	}

	default ParticleBoolean above(ParticleDouble threshold) {
		return particle -> compute(particle) > threshold.compute(particle);
	}

	default ParticleBoolean above(double value) {
		return particle -> compute(particle) > value;
	}
}
