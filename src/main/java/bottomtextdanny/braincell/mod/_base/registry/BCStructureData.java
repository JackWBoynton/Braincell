package bottomtextdanny.braincell.mod._base.registry;

import bottomtextdanny.braincell.base.pair.Pair;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface BCStructureData<T extends StructureFeature<?>> {

    record Pre<T extends StructureFeature<?>>(List<Pair<ResourceLocation, Supplier<ConfiguredStructureFeature<?, ?>>>> confGetter,
                                              ResourceLocation setKey, Function<BCStructureRegistry<T>, StructureSet> setMod)
            implements BCStructureData<T> {

        public Post<T> makePost(BiFunction<ResourceLocation, Supplier<ConfiguredStructureFeature<?, ?>>, Holder<ConfiguredStructureFeature<?, ?>>> confActor) {
            return new Post<T>(
                    this.confGetter.stream().map(p -> confActor.apply(p.left(), p.right())).toList());
        }
    }

    record Post<T extends StructureFeature<?>>(List<Holder<ConfiguredStructureFeature<?, ?>>> confGetter)
            implements BCStructureData<T> {
    }
}
