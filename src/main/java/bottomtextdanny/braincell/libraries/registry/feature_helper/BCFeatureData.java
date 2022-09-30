package bottomtextdanny.braincell.libraries.registry.feature_helper;

import bottomtextdanny.braincell.base.pair.Pair;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public interface BCFeatureData<T extends Feature<?>> {

    record Pre<T extends Feature<?>>(List<Pair<ResourceLocation, Supplier<ConfiguredFeature<?, ?>>>> confGetter,
                                     List<Pair<ResourceLocation, Supplier<PlacedFeature>>> placedGetter)
            implements BCFeatureData<T> {

        public Config<T> makeConfigured(BiFunction<ResourceLocation, Supplier<ConfiguredFeature<?, ?>>, Holder<ConfiguredFeature<?, ?>>> confActor) {
            return new Config<>(
                    this.confGetter.stream().map((p) -> confActor.apply(p.left(), p.right())).toList(),
                    this.placedGetter);
        }
    }

    record Config<T extends Feature<?>>(List<Holder<ConfiguredFeature<?, ?>>> confGetter,
                                        List<Pair<ResourceLocation, Supplier<PlacedFeature>>> placedGetter)
            implements BCFeatureData<T>, Configured {

        public Post<T> makePost(BiFunction<ResourceLocation, Supplier<PlacedFeature>, Holder<PlacedFeature>> placedActor) {
            return new Post<>(
                    this.confGetter,
                    this.placedGetter.stream().map((p) -> placedActor.apply(p.left(), p.right())).toList());
        }
    }

    record Post<T extends Feature<?>>(List<Holder<ConfiguredFeature<?, ?>>> confGetter,
                                      List<Holder<PlacedFeature>> placedGetter)
            implements BCFeatureData<T>, Configured {
    }

    interface Configured {
        List<Holder<ConfiguredFeature<?, ?>>> confGetter();
    }
}
