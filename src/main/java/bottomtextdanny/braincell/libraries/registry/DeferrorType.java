package bottomtextdanny.braincell.libraries.registry;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public final class DeferrorType<T> {
    private static final Map<ResourceLocation, DeferrorType<?>> BY_KEY = Maps.newHashMap();
    private static final MutableInt ID_PROVIDER = new MutableInt(0);
    //*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//
    public static final DeferrorType<SoundEvent> SOUND_EVENT =
            make(new ResourceLocation("builtin:sound_event"), Registry.SOUND_EVENT_REGISTRY);
    public static final DeferrorType<Attribute> ATTRIBUTE =
            make(new ResourceLocation("builtin:attribute"), Registry.ATTRIBUTE_REGISTRY);
    public static final DeferrorType<EntityType<?>> ENTITY_TYPE =
            make(new ResourceLocation("builtin:entity_type"), Registry.ENTITY_TYPE_REGISTRY);
    public static final DeferrorType<Feature<?>> FEATURE =
            make(new ResourceLocation("builtin:feature"), Registry.FEATURE_REGISTRY);
    public static final DeferrorType<BlockPredicateType<?>> BLOCK_PREDICATE_TYPE =
            make(new ResourceLocation("builtin:block_predicate_type"), Registry.BLOCK_PREDICATE_TYPE_REGISTRY);
    public static final DeferrorType<Biome> BIOME =
            make(new ResourceLocation("builtin:tagKey"), Registry.BIOME_REGISTRY);
    public static final DeferrorType<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZER =
            make(new ResourceLocation("builtin:biome_modifier_serializer"), ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS);
    public static final DeferrorType<BiomeModifier> BIOME_MODIFIER =
            make(new ResourceLocation("builtin:biome_modifier"), ForgeRegistries.Keys.BIOME_MODIFIERS);
    public static final DeferrorType<StructureType<?>> STRUCTURE_TYPE =
            make(new ResourceLocation("builtin:structure_type"), Registry.STRUCTURE_TYPE_REGISTRY);
    public static final DeferrorType<StructurePieceType> STRUCTURE_PIECE =
            make(new ResourceLocation("builtin:structure_piece"), Registry.STRUCTURE_PIECE_REGISTRY);
    public static final DeferrorType<Structure> STRUCTURE =
            make(new ResourceLocation("builtin:structure"), Registry.STRUCTURE_REGISTRY);
    public static final DeferrorType<Block> BLOCK =
            make(new ResourceLocation("builtin:block"), Registry.BLOCK_REGISTRY);
    public static final DeferrorType<Item> ITEM =
            make(new ResourceLocation("builtin:item"), Registry.ITEM_REGISTRY);
    public static final DeferrorType<MenuType<?>> MENU_TYPE =
            make(new ResourceLocation("builtin:menu_type"), Registry.MENU_REGISTRY);
    public static final DeferrorType<MobEffect> MOB_EFFECT =
            make(new ResourceLocation("builtin:mob_effect"), Registry.MOB_EFFECT_REGISTRY);
    public static final DeferrorType<BlockEntityType<?>> BLOCK_ENTITY_TYPE =
            make(new ResourceLocation("builtin:block_entity_type"), Registry.BLOCK_ENTITY_TYPE_REGISTRY);
    public static final DeferrorType<ParticleType<?>> PARTICLE_TYPE =
            make(new ResourceLocation("builtin:particle_type"), Registry.PARTICLE_TYPE_REGISTRY);
    public static final DeferrorType<RecipeSerializer<?>> RECIPE_SERIALIZER =
            make(new ResourceLocation("builtin:recipe_serializer"), Registry.RECIPE_SERIALIZER_REGISTRY);
    public static final DeferrorType<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPE =
            make(new ResourceLocation("builtin:argument_type"), Registry.COMMAND_ARGUMENT_TYPE_REGISTRY);
    //*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//
    private final ResourceLocation key;
    private final int id;
    private final ResourceKey<Registry<T>> registry;

    public DeferrorType(ResourceLocation key, ResourceKey<Registry<T>> registry) {
        super();
        this.key = key;
        this.id = ID_PROVIDER.getAndIncrement();
        this.registry = registry;
    }

    private static <U> DeferrorType<U> make(ResourceLocation key, ResourceKey<Registry<U>> classRef) {
        DeferrorType<U> type = new DeferrorType<>(key, classRef);
        BY_KEY.put(key, type);
        return type;
    }

    public ResourceKey<Registry<T>> getRegistry() {
        return this.registry;
    }

    public int getId() {
        return this.id;
    }

    public static DeferrorType<?> getByKey(ResourceLocation key) {
        return BY_KEY.get(key);
    }

    public static Collection<DeferrorType<?>> collection() {
        return Collections.unmodifiableCollection(BY_KEY.values());
    }

    public ResourceLocation getKey() {
        return this.key;
    }
}
