package bottomtextdanny.braincell.tables;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.chart.segment.*;
import bottomtextdanny.braincell.libraries.chart.segment_maker.SegmentPregenerations;
import bottomtextdanny.braincell.libraries.registry.*;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import static bottomtextdanny.braincell.libraries.registry.Serializer.pack;
import static bottomtextdanny.braincell.libraries.registry.Serializer.unpack;

public class BCContextualSerializers {
    public static final ResourceKey<Registry<ContextualSerializer<?, ?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Braincell.ID, "contextual_serializer"));
    private static final DeferredRegister<ContextualSerializer<?, ?>> DEFERRED_REGISTERER = DeferredRegister.create(REGISTRY_KEY, Braincell.ID);
    public final static Supplier<IForgeRegistry<ContextualSerializer<?, ?>>> REGISTRY = DEFERRED_REGISTERER.makeRegistry(() -> new RegistryBuilder<>());
    public static final BCRegistry<ContextualSerializer<?, ?>> ENTRIES = new BCRegistry<>();
    public static final RegistryHelper<ContextualSerializer<?, ?>> HELPER = new RegistryHelper<>(Braincell.DEFERRING_STATE, ENTRIES);

    public static final Wrap<ContextualSerializer<CollisionManager, Segment<?>>> COLLISION_MANAGER = HELPER.defer("collision_manager", () -> new ContextualSerializer<>(
            AbstractCollisionManager::serializeBase,
            (tag, base) -> new CollisionManager((CompoundTag) tag)
    ));

    public static final Wrap<ContextualSerializer<SegmentTicket, SegmentAdmin>> SEGMENT_TICKET = HELPER.defer("segment_ticket", () -> new ContextualSerializer<>(
            (obj, admin) -> {
                CompoundTag tag = new CompoundTag();
                tag.put("pos", Serializer.packDirectly(obj.position(), BCSerializers.BLOCKPOS.get()));
                tag.put("pregen", pack(obj.modifier()));
                tag.put("rotation", Serializer.packDirectly(obj.rotation(), BCSerializers.ROTATION.get()));
                tag.put("segment", pack(obj.segment(), admin));

                return tag;
            },
            (tagUncasted, admin) -> {
                CompoundTag tag = (CompoundTag) tagUncasted;
                return SegmentTicket.orientable(
                        Serializer.unpackDirectly(tag.get("pos"), BCSerializers.BLOCKPOS.get()),
                        Serializer.unpack(tag.get("pregen"), SegmentPregenerations.spgCancel()),
                        Serializer.unpackDirectly(tag.get("rotation"), BCSerializers.ROTATION.get()),
                        unpack(tag.get("segment"), (Segment<?>) null, admin)
                );
            }
    ));

    public static final Wrap<ContextualSerializer<IndependantSegment, SegmentAdmin>> INDEPENDENT_SEGMENT = HELPER.defer("independent_segment", () -> segmentSerializer(IndependantSegment::new));
    public static final Wrap<ContextualSerializer<BoxSegment, SegmentAdmin>> BOX_SEGMENT = HELPER.defer("box_segment", () -> segmentSerializer(BoxSegment::new));
    public static final Wrap<ContextualSerializer<SchemaSegment, SegmentAdmin>> SCHEMA_SEGMENT = HELPER.defer("schema_segment", () -> segmentSerializer(SchemaSegment::new));

    private static <T extends Segment<?>> ContextualSerializer<T, SegmentAdmin> segmentSerializer(BiFunction<CompoundTag, SegmentAdmin, T> getter) {
        return new ContextualSerializer<>(
                (obj, admin) -> obj.childData(),
                (tagUncasted, admin) -> {
                    if (tagUncasted instanceof CompoundTag tag)
                        return getter.apply(tag, admin);
                    return null;
                }
        );
    }

    public static void registerRegistry() {
        DEFERRED_REGISTERER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
