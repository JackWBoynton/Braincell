package bottomtextdanny.braincell.libraries.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ModularParticleData implements ParticleOptions {
    public static final Deserializer<ModularParticleData> DESERIALIZER = new Deserializer<ModularParticleData>() {
        public ModularParticleData fromCommand(ParticleType<ModularParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            return ((ModularParticleType)particleTypeIn).getDefaultData();
        }

        public ModularParticleData fromNetwork(ParticleType<ModularParticleData> particleTypeIn, FriendlyByteBuf buffer) {
            return ((ModularParticleType)particleTypeIn).getDefaultData();
        }
    };
    private final Codec<ModularParticleData> codec = Codec.unit(this);
    private final @Nullable List<Object> data;
    ModularParticleType particleType;

    public ModularParticleData(ModularParticleType type, Object... parameters) {
        data = Arrays.asList(parameters);
        particleType = type;
    }

    public ModularParticleData(ModularParticleType type) {
        this();
        particleType = type;
    }

    private ModularParticleData(Object... parameters) {
        data = Arrays.asList(parameters);
    }

    public static ModularParticleData defaulted(Object... parameters) {
        return new ModularParticleData(parameters);
    }

    @Override
    public ModularParticleType getType() {
        return particleType;
    }

    public List<Object> getData() {
        if (data == null) return null;
        return Collections.unmodifiableList(data);
    }

    public Codec<ModularParticleData> getCodec() {
        return codec;
    }

    public Object getParam(int index) {
        return data.get(index);
    }

    @SuppressWarnings("unchecked")
    public <T> T fetch(int index, Class<T> cast) {
        return (T) data.get(index);
    }

    @SuppressWarnings("unchecked")
    public <T> T fetch(int index) {
        return (T) data.get(index);
    }

    public int dataSize() {
        return data.size();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
    }

    @Override
    public String writeToString() {
        return "Braincell Modular Particle";
    }
}
