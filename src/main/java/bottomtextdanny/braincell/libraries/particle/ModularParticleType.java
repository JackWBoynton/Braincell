package bottomtextdanny.braincell.libraries.particle;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatList;
import it.unimi.dsi.fastutil.floats.FloatLists;
import net.minecraft.core.particles.ParticleType;

public class ModularParticleType extends ParticleType<ModularParticleOptions<?>> {
    private final Codec<ModularParticleOptions<?>> codec;
    private final ModularParticleOptions<?> defaultData;
    public final int sprites;

    public ModularParticleType(boolean alwaysShow, ModularParticleOptions<?> data) {
        super(alwaysShow, ModularParticleOptions.DESERIALIZER);
        defaultData = data;
        defaultData.particleType = this;
        sprites = 1;
        codec = Codec.unit(defaultData);
    }

    public ModularParticleType(int sprites, boolean alwaysShow, ModularParticleOptions<?> data) {
        super(alwaysShow, ModularParticleOptions.DESERIALIZER);
        defaultData = data;
        defaultData.particleType = this;
        this.sprites = sprites;
        codec = Codec.unit(defaultData);
    }

    public ModularParticleOptions<?> geDefaultOptions() {
        return defaultData;
    }

    @Override
    public Codec<ModularParticleOptions<?>> codec() {
        return codec;
    }
}
