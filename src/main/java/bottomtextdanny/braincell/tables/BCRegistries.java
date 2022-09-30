package bottomtextdanny.braincell.tables;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.registry.ContextualSerializer;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public final class BCRegistries {
    public static final ResourceKey<Registry<Serializer<?>>> SERIALIZER_REGISTRY = key("serializer");
    public static final Supplier<IForgeRegistry<Serializer<?>>> SERIALIZERS = registry(SERIALIZER_REGISTRY);

    public static final ResourceKey<Registry<ContextualSerializer<?, ?>>> CONTEXTUAL_SERIALIZER_REGISTRY = key("contextual_serializer");
    public static final Supplier<IForgeRegistry<ContextualSerializer<?, ?>>> CONTEXTUAL_SERIALIZERS = registry(CONTEXTUAL_SERIALIZER_REGISTRY);

//    public static final ResourceKey<Registry<Serializer<? extends Distance2>>> DISTANCE2_REGISTRY = key("distance2_serializer");
//    public static final IForgeRegistry<Serializer<? extends Distance2>> DISTANCE2S = registry(DISTANCE2_REGISTRY);
//
//    public static final ResourceKey<Registry<Serializer<? extends Distance3>>> DISTANCE3_REGISTRY = key("distance3_serializer");
//    public static final IForgeRegistry<Serializer<? extends Distance3>> DISTANCE3S = registry(DISTANCE3_REGISTRY);

//    public static final ResourceKey<Registry<Serializer<? extends GridSampler>>> GRID_SAMPLER_REGISTRY = key("grid_sampler_serializer");
//    public static final IForgeRegistry<Serializer<? extends GridSampler>> GRID_SAMPLERS = registry(GRID_SAMPLER_REGISTRY);
//    public static final ResourceKey<Registry<Serializer<? extends GridWarper>>> GRID_WARPER_REGISTRY = key("grid_warper_serializer");
//    public static final IForgeRegistry<Serializer<? extends GridWarper>> GRID_WARPERS = registry(GRID_WARPER_REGISTRY);
//    public static final ResourceKey<Registry<Serializer<? extends SegmentDecorator<?>>>> SEGMENT_DECORATOR_REGISTRY = key("segment_decorator");
//    public static final IForgeRegistry<Serializer<? extends SegmentDecorator<?>>> SEGMENT_DECORATORS = registry(SEGMENT_DECORATOR_REGISTRY);
//    public static final ResourceKey<Registry<Serializer<? extends SegmentPregen>>> SEGMENT_PREGEN_REGISTRY = key("segment_pregen");
//    public static final IForgeRegistry<Serializer<? extends SegmentPregen>> SEGMENT_PREGEN = registry(SEGMENT_PREGEN_REGISTRY);
//    public static final ResourceKey<Registry<Serializer<? extends LevelBlockPredicate>>> LEVEL_BLOCK_PREDICATE_REGISTRY = key("level_block_predicate");
//    public static final IForgeRegistry<Serializer<? extends LevelBlockPredicate>> LEVEL_BLOCK_PREDICATES = registry(LEVEL_BLOCK_PREDICATE_REGISTRY);
//    public static final ResourceKey<Registry<ContextualSerializer<? extends SegmentAdmin, BlockPos>>> SEGMENT_ADMIN_REGISTRY = key("segment_admin");
//    public static final IForgeRegistry<ContextualSerializer<? extends SegmentAdmin, BlockPos>> SEGMENT_ADMINS = registry(SEGMENT_ADMIN_REGISTRY);
//    public static final ResourceKey<Registry<Serializer<? extends SegmentPredicate>>> SEGMENT_PREDICATE_REGISTRY = key("segment_predicate");
//    public static final IForgeRegistry<Serializer<? extends SegmentPredicate>> SEGMENT_PREDICATES = registry(SEGMENT_PREDICATE_REGISTRY);

    public static void load() {}

    private static <T> ResourceKey<Registry<T>> key(String name) {
        return ResourceKey.createRegistryKey(new ResourceLocation(Braincell.ID, name));
    }

    private static <T> Supplier<IForgeRegistry<T>> registry(ResourceKey<? extends Registry<T>> key) {
        return DeferredRegister.create(key, Braincell.ID).makeRegistry(() -> new RegistryBuilder<T>());
    }
}
