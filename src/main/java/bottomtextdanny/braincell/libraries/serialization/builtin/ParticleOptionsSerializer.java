package bottomtextdanny.braincell.libraries.serialization.builtin;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import bottomtextdanny.braincell.libraries.serialization.SimpleDataSerializer;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Optional;

public final class ParticleOptionsSerializer implements DataSerializer<ParticleOptions> {
    public static final ResourceLocation REF =
            new ResourceLocation(Braincell.ID, "particle_options");

    @Override
    public void writeNBT(CompoundTag nbt, ParticleOptions obj, String storage) {
        nbt.putString(storage, obj.writeToString());
    }

    @Nullable
    @Override
    public ParticleOptions readNBT(CompoundTag nbt, String storage) {
        if (!nbt.contains(storage)) return null;
        String particleConfig = nbt.getString(storage);
        try {
            return ParticleArgument.readParticle(new StringReader(nbt.getString(storage)));
        } catch (CommandSyntaxException e) {
            LOGGER.warn("Couldn't load custom particle {}", particleConfig, e);
        }
        return null;
    }

    @Override
    public void writePacketStream(FriendlyByteBuf stream, ParticleOptions obj) {
        stream.writeInt(Registry.PARTICLE_TYPE.getId(obj.getType()));
        obj.writeToNetwork(stream);
    }

    @Override
    public ParticleOptions readPacketStream(FriendlyByteBuf stream) {
        Optional<ParticleType<?>> particleTypeOp = Optional.ofNullable(Registry.PARTICLE_TYPE.byId(stream.readInt()));
        try {
            return particleTypeOp.isPresent() ? readParticleFromStream(stream, particleTypeOp.get()) : null;
        } catch (Exception e) {
            LOGGER.warn("Couldn't load custom particle from client", e);
        }
        return null;
    }

    private <T extends ParticleOptions> T readParticleFromStream(FriendlyByteBuf stream, ParticleType<T> type) {
        return type.getDeserializer().fromNetwork(type, stream);
    }

    @Override
    public ResourceLocation key() {
        return REF;
    }
}
