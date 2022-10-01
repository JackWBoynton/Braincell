/*
 * Copyright Saturday October 01 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.tables;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.network.Connection;
import bottomtextdanny.braincell.libraries.particle.ModularParticleData;
import bottomtextdanny.braincell.libraries.particle.ModularParticleType;
import bottomtextdanny.braincell.libraries.particle.client.OpaqueParticle;
import bottomtextdanny.braincell.libraries.particle.client.limb.Limb;
import bottomtextdanny.braincell.libraries.particle.client.limb.LimbParticle;
import bottomtextdanny.braincell.libraries.particle.client.tickers.ParticleAction;
import bottomtextdanny.braincell.libraries.registry.BCRegistry;
import bottomtextdanny.braincell.libraries.registry.RegistryHelper;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Function;
import java.util.function.Supplier;

public final class BCParticles {
	public static final BCRegistry<ParticleType<?>> ENTRIES = new BCRegistry<>();
	public static final RegistryHelper<ParticleType<?>> HELPER = new RegistryHelper<>(Braincell.DEFERRING_STATE, ENTRIES);

	public static final Wrap<ModularParticleType> LIMB = defer("limb",
		() -> new ModularParticleType(true, ModularParticleData.defaulted(ParticleAction.NO, ParticleAction.NO, null)),
		() -> o -> new LimbParticle.Factory((SpriteSet) o));
	public static final Wrap<ModularParticleType> DUST = defer("dust",
		() -> new ModularParticleType(1, true, ModularParticleData.defaulted(ParticleAction.NO, ParticleAction.NO)),
		() -> o -> new OpaqueParticle.Factory((SpriteSet) o));
	public static final Wrap<ModularParticleType> CLOUD = defer("cloud",
		() -> new ModularParticleType(8, true, ModularParticleData.defaulted(ParticleAction.NO, ParticleAction.NO)),
		() -> o -> new OpaqueParticle.Factory((SpriteSet) o));

	private static <D extends ParticleOptions, T extends ParticleType<D>> Wrap<T> defer(String key, Supplier<T> particleType, Supplier<? extends Function<Object, Object>> factory) {
		Wrap<T> wrapped = HELPER.defer(key, particleType);
		Connection.doClientSide(() -> {
			Braincell.client().getParticleFactoryDeferror().addFactoryStitch(wrapped, spriteSet -> (ParticleProvider<D>)factory.get().apply(spriteSet));
		});
		return wrapped;
	}
}
