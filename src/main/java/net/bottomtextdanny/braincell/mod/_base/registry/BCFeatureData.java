package net.bottomtextdanny.braincell.mod._base.registry;

import net.bottomtextdanny.braincell.base.pair.Pair;
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

        public Post<T> makePost(BiFunction<ResourceLocation, Supplier<ConfiguredFeature<?, ?>>, Holder<ConfiguredFeature<?, ?>>> confActor,
                                BiFunction<ResourceLocation, Supplier<PlacedFeature>, Holder<PlacedFeature>> placedActor) {
            return new Post<>(
                    this.confGetter.stream().map((p) -> confActor.apply(p.left(), p.right())).toList(),
                    this.placedGetter.stream().map((p) -> placedActor.apply(p.left(), p.right())).toList());
        }
    }

    record Post<T extends Feature<?>>(List<Holder<ConfiguredFeature<?, ?>>> confGetter,
                                               List<Holder<PlacedFeature>> placedGetter)
            implements BCFeatureData<T> {
    }
}
