package net.bottomtextdanny.braincell.mod._base.registry;

import com.google.common.collect.Lists;
import net.bottomtextdanny.braincell.base.BCSuppliers;
import net.bottomtextdanny.braincell.base.pair.Pair;
import net.bottomtextdanny.braincell.mixin.StructureFeatureMixin;
import net.bottomtextdanny.braincell.mod._base.registry.managing.Wrap;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

public final class BCStructureRegistry<T extends StructureFeature<?>> extends Wrap<T> {
    private final List<StructurePieceType> pieces;
    private ResourceKey<StructureSet> structureSetRegistry;
    private Holder<StructureSet> structureSet;
    private BCStructureData<T> data;
    private boolean built = false;

    public BCStructureRegistry(ResourceLocation name,
                               Supplier<T> sup,
                               GenerationStep.Decoration step,
                               List<StructurePieceType> pieces,
                               List<Pair<ResourceLocation, Function<BCStructureRegistry<T>, ConfiguredStructureFeature<?, ?>>>> configured,
                               ResourceLocation setKey, Function<BCStructureRegistry<T>, StructureSet> setMod) {
        super(name, sup);
        this.pieces = pieces;
        this.data = new BCStructureData.Pre<T>(
                configured.stream().map(p -> Pair.of(p.left(), BCSuppliers.compose(p.right(), this))).toList(), setKey, setMod);
        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLCommonSetupEvent e) -> {
            StructureFeatureMixin.getSTEP().put(this.obj, step);
        });
    }

    public static <T extends StructureFeature<?>> Builder<T> builder(ResourceLocation key, Supplier<T> sup, GenerationStep.Decoration step) {
        return new Builder<>(key, sup, step);
    }

    public StructurePieceType getPiece(int index) {
        if (this.built) {
            return this.pieces.get(index);
        } else throw unbuiltException();
    }

    public Holder<ConfiguredStructureFeature<?, ?>> getConfigured(int index) {
        if (this.built) {
            return ((BCStructureData.Post<T>)this.data).confGetter().get(index);
        } else throw unbuiltException();
    }

    @Override
    public void solve() {
        super.solve();
        makeExtraRegistries();
    }

    public void makeExtraRegistries() {
        if (this.data instanceof BCStructureData.Pre<T> pre) {
            this.built = true;
            this.data = pre.makePost(
                    (rl, type) -> BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, rl, type.get()));
            this.structureSetRegistry = ResourceKey.create(Registry.STRUCTURE_SET_REGISTRY, pre.setKey());
            this.structureSet = BuiltinRegistries.register(BuiltinRegistries.STRUCTURE_SETS, this.structureSetRegistry, pre.setMod().apply(this));
        }
    }

    private static IllegalStateException unbuiltException() {
        return new IllegalStateException("Extra structure registries are yet to be built.");
    }

    public static class Builder<T extends StructureFeature<?>> {
        private final ResourceLocation key;
        private final Supplier<T> supplier;
        private final GenerationStep.Decoration step;
        private final List<StructurePieceType> pieces;
        private final List<Pair<ResourceLocation, Function<BCStructureRegistry<T>, ConfiguredStructureFeature<?, ?>>>> confGetter;
        private ResourceLocation structureSetKey;
        private Function<BCStructureRegistry<T>, StructureSet> structureSetGetter;

        Builder(ResourceLocation key, Supplier<T> supplier, GenerationStep.Decoration step) {
            super();
            this.key = key;
            this.supplier = supplier;
            this.step = step;
            this.pieces = Lists.newLinkedList();
            this.confGetter = Lists.newLinkedList();
        }

        public Builder<T> piece(String key, StructurePieceType type) {
            this.pieces.add(Registry.register(Registry.STRUCTURE_PIECE, key.toLowerCase(Locale.ROOT), type));
            return this;
        }

        public Builder<T> configured(ResourceLocation key, Function<BCStructureRegistry<T>, ConfiguredStructureFeature<?, ?>> type) {
            this.confGetter.add(Pair.of(key, type));
            return this;
        }

        public Builder<T> set(ResourceLocation structureSetKey, Function<BCStructureRegistry<T>, StructureSet> structureSetFunction) {
            this.structureSetKey = structureSetKey;
            this.structureSetGetter = structureSetFunction;
            return this;
        }

        public BCStructureRegistry<T> build() {
            return new BCStructureRegistry<>(this.key, this.supplier, this.step, this.pieces, this.confGetter, this.structureSetKey, this.structureSetGetter);
        }
    }
}
