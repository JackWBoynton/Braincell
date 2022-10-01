/*
 * Copyright Saturday October 01 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.particle.client.tickers;

import bottomtextdanny.braincell.libraries.particle.client.MParticle;

import java.util.LinkedList;
import java.util.List;

@FunctionalInterface
public interface ParticleAction {
	ParticleAction NO = particle -> {};

	void execute(MParticle particle);

	default ParticleAction append(ParticleAction other1) {
		return new ParticleAction() {
			@Override
			public void execute(MParticle particle) {
				ParticleAction.this.execute(particle);
				other1.execute(particle);
			}

			@Override
			public ParticleAction append(ParticleAction other2) {
				return new ParticleAction() {
					@Override
					public void execute(MParticle particle) {
						ParticleAction.this.execute(particle);
						other1.execute(particle);
						other2.execute(particle);
					}

					@Override
					public ParticleAction append(ParticleAction other3) {
						List<ParticleAction> list = new LinkedList<>();
						list.add(ParticleAction.this);
						list.add(other1);
						list.add(other2);
						list.add(other3);
						return new ParticleAction() {

							@Override
							public void execute(MParticle particle) {
								for (ParticleAction action : list) {
									action.execute(particle);
								}
							}

							@Override
							public ParticleAction append(ParticleAction other4) {
								list.add(other4);
								return this;
							}
						};
					}
				};
			}
		};
	}
}
