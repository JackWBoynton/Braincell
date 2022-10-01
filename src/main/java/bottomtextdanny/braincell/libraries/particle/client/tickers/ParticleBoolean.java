/*
 * Copyright Saturday October 01 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.particle.client.tickers;

import bottomtextdanny.braincell.libraries.particle.client.MParticle;

public interface ParticleBoolean {

	boolean compute(MParticle particle);

	default ParticleBoolean or(ParticleBoolean other) {
		return particle -> compute(particle) || other.compute(particle);
	}

	default ParticleBoolean and(ParticleBoolean other) {
		return particle -> compute(particle) && other.compute(particle);
	}

	default ParticleBoolean neg() {
		return particle -> !compute(particle);
	}
}
