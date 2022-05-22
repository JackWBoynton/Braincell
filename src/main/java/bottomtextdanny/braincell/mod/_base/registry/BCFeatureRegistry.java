package bottomtextdanny.braincell.mod._base.registry;

import bottomtextdanny.braincell.base.pair.Pair;
import bottomtextdanny.braincell.mod._base.registry.managing.Wrap;
import com.google.common.collect.Lists;
import bottomtextdanny.braincell.base.BCSuppliers;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public final class BCFeatureRegistry<T extends Feature<?>> extends Wrap<T> {
    private BCFeatureData<T> data;
    private boolean built = false;

    public BCFeatureRegistry(ResourceLocation name,
                             Supplier<T> sup,
                             List<Pair<ResourceLocation, Function<BCFeatureRegistry<T>, ConfiguredFeature<?, ?>>>> configured,
                             List<Pair<ResourceLocation, Function<BCFeatureRegistry<T>, PlacedFeature>>> placed) {
        super(name, sup);
        this.data = new BCFeatureData.Pre<T>(
                configured.stream().map(p ->  Pair.of(p.left(), BCSuppliers.compose(p.right(), this))).toList(),
                placed.stream().map(p -> Pair.of(p.left(), BCSuppliers.compose(p.right(), this))).toList());
    }

    public static <T extends Feature<?>> Builder<T> builder(ResourceLocation key, Supplier<T> sup, GenerationStep.Decoration step) {
        return new Builder<>(key, sup, step);
    }

    public Holder<ConfiguredFeature<?, ?>> getConfigured(int index) {
        if (this.built) {
            return ((BCFeatureData.Post<T>)this.data).confGetter().get(index);
        } else throw unbuiltException();
    }

    @Override
    public void solve() {
        super.solve();
        makeExtraRegistries();
    }

    public void makeExtraRegistries() {
        if (this.data instanceof BCFeatureData.Pre<T> pre) {
            this.built = true;
            this.data = pre.makePost(
                    (rl, sup) -> BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, rl, sup.get()),
                    (rl, sup) -> BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, rl, sup.get()));
        }
    }

    private static IllegalStateException unbuiltException() {
        return new IllegalStateException("Extra structure registries are yet to be built.");
    }

    public static class Builder<T extends Feature<?>> {
        private final ResourceLocation key;
        private final Supplier<T> supplier;
        private final List<Pair<ResourceLocation, Function<BCFeatureRegistry<T>, ConfiguredFeature<?, ?>>>> confGetter;
        private final List<Pair<ResourceLocation, Function<BCFeatureRegistry<T>, PlacedFeature>>> placedGetter;

        Builder(ResourceLocation key, Supplier<T> supplier, GenerationStep.Decoration step) {
            super();
            this.key = key;
            this.supplier = supplier;
            this.confGetter = Lists.newLinkedList();
            this.placedGetter = Lists.newLinkedList();
        }

        public Builder<T> configured(ResourceLocation key, Function<BCFeatureRegistry<T>, ConfiguredFeature<?, ?>> type) {
            this.confGetter.add(Pair.of(key, type));
            return this;
        }

        public Builder<T> placed(ResourceLocation key, int confIndex, Function<Holder<ConfiguredFeature<?, ?>>, PlacedFeature> type) {
            this.placedGetter.add(Pair.of(key, type.compose(r -> r.getConfigured(confIndex))));
            return this;
        }

        public BCFeatureRegistry<T> build() {
            return new BCFeatureRegistry<>(this.key, this.supplier, this.confGetter, this.placedGetter);
        }
    }
}
