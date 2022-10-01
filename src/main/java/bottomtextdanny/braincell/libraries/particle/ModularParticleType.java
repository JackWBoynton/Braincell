package bottomtextdanny.braincell.libraries.particle;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;

public class ModularParticleType extends ParticleType<ModularParticleData> {
    private final ModularParticleData defaultData;
    public final int sprites;

    public ModularParticleType(boolean alwaysShow, ModularParticleData data) {
        super(alwaysShow, ModularParticleData.DESERIALIZER);
        defaultData = data;
        defaultData.particleType = this;
        sprites = 1;
    }

    public ModularParticleType(int sprites, boolean alwaysShow, ModularParticleData data) {
        super(alwaysShow, ModularParticleData.DESERIALIZER);
        defaultData = data;
        defaultData.particleType = this;
        this.sprites = sprites;
    }

    public ModularParticleData getDefaultData() {
        return defaultData;
    }

    public Object getParam(int index) {
        return defaultData.getParam(index);
    }

    @Override
    public Codec<ModularParticleData> codec() {
        return defaultData.getCodec();
    }
}
