package bottomtextdanny.braincell.mod._mod.common_sided.events;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraftforge.eventbus.api.Event;

import java.util.function.BiConsumer;

public final class StructureBiomeStitchEvent extends Event {
    private final BiConsumer<ConfiguredStructureFeature<?,?>, ResourceKey<Biome>> registerFunction;

    public StructureBiomeStitchEvent(BiConsumer<ConfiguredStructureFeature<?,?>, ResourceKey<Biome>> registerFunction) {
        super();
        this.registerFunction = registerFunction;
    }

    public void register(ConfiguredStructureFeature<?,?> feature, ResourceKey<Biome> biomeKey) {
        this.registerFunction.accept(feature, biomeKey);
    }
}
