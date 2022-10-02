/*
 * Copyright Saturday October 01 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.particle.client.tickers;

import bottomtextdanny.braincell.libraries.particle.client.MParticle;

import java.util.LinkedList;
import java.util.List;

@FunctionalInterface
public interface ParticleAction<T extends MParticle> {
	ParticleAction NO = particle -> {};

	default void _execute(MParticle particle) {
		execute((T)particle);
	}

	void execute(T particle);
}
