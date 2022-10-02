package bottomtextdanny.braincell.libraries.particle;

import bottomtextdanny.braincell.libraries.particle.client.MParticle;
import bottomtextdanny.braincell.libraries.particle.client.tickers.ParticleAction;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ModularParticleOptions<E extends ExtraOptions> implements ParticleOptions {
    public static final Deserializer<ModularParticleOptions<?>> DESERIALIZER = new Deserializer<>() {
        public ModularParticleOptions<?> fromCommand(ParticleType<ModularParticleOptions<?>> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            return ((ModularParticleType) particleTypeIn).geDefaultOptions();
        }

        public ModularParticleOptions<?> fromNetwork(ParticleType<ModularParticleOptions<?>> particleTypeIn, FriendlyByteBuf buffer) {
            return ((ModularParticleType) particleTypeIn).geDefaultOptions();
        }
    };
    ModularParticleType particleType;
    public final ParticleAction<?> start;
    public final ParticleAction<?> tick;
    public final E extra;

    @OnlyIn(Dist.CLIENT)
    public <Y extends MParticle> ModularParticleOptions(ModularParticleType type, ParticleAction<Y> start, ParticleAction<Y> tick) {
        this(type, start, tick, (E)type.geDefaultOptions().extra);
    }

    @OnlyIn(Dist.CLIENT)
    public <Y extends MParticle> ModularParticleOptions(ModularParticleType type, ParticleAction<Y> start, ParticleAction<Y> tick, E extra) {
        particleType = type;
        this.start = start;
        this.tick = tick;
        this.extra = extra;
    }

    private ModularParticleOptions(ParticleAction<?> start, ParticleAction<?> tick, E extra) {
        this.start = start;
        this.tick = tick;
        this.extra = extra;
    }

    public static <E extends ExtraOptions> ModularParticleOptions<E> defaulted(ParticleAction<?> start, ParticleAction<?> tick, E extra) {
        return new ModularParticleOptions<>(start, tick, extra);
    }

    public static  ModularParticleOptions<ExtraOptions> defaulted(ParticleAction<?> start, ParticleAction<?> tick) {
        return new ModularParticleOptions<>(start, tick, ExtraOptions.IMPL);
    }

    @Override
    public ModularParticleType getType() {
        return particleType;
    }

    public ParticleAction<?> getStart() {
        return start;
    }

    public ParticleAction<?> getTick() {
        return tick;
    }

    public E getExtra() {
        return extra;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
    }

    @Override
    public String writeToString() {
        return "Braincell Modular Particle";
    }
}
