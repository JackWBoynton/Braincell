package bottomtextdanny.braincell.libraries.registry;

import bottomtextdanny.braincell.libraries.entity_variant.IndexedVariableModule;
import bottomtextdanny.braincell.libraries.entity_variant.StringedVariableModule;
import bottomtextdanny.braincell.libraries.entity_variant.VariableModule;
import bottomtextdanny.braincell.libraries.entity_variant.VariantProvider;
import bottomtextdanny.braincell.tables.BCContextualSerializers;
import bottomtextdanny.braincell.tables.BCRegistries;
import bottomtextdanny.braincell.tables.BCSerializers;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

import java.util.Optional;
import java.util.function.Function;

public record Serializer<T>(Function<T, Tag> toTag, Function<Tag, T> fromTag) {

    public Tag toTagUnsafe(Object obj) {
        return toTag.apply((T)obj);
    }

    public Tag toTag(T obj) {
        return toTag.apply(obj);
    }

    public T fromTag(Tag obj) {
        return fromTag.apply(obj);
    }

    public static <T> Tag packDirectly(T obj, Serializer<T> serializer) {

        return serializer.toTagUnsafe(obj);
    }

    public static <T, B> Tag packDirectly(T obj, B base, ContextualSerializer<T, B> serializer) {
        return serializer.toTag().apply(obj, base);
    }

    public static CompoundTag pack(Wrap<? extends Serializer<?>> serializer, Object obj) {
        CompoundTag root = new CompoundTag();

        root.put("0", StringTag.valueOf(serializer.getKey().toString()));
        root.put("1", serializer.get().toTagUnsafe(obj));

        return root;
    }

    public static CompoundTag pack(Serializable obj) {
        CompoundTag root = new CompoundTag();

        root.put("0", StringTag.valueOf(obj.serializer().getKey().toString()));
        root.put("1", (obj.serializer()).get().toTagUnsafe(obj));

        return root;
    }

    public static <B> CompoundTag pack(ContextualSerializable<B> obj, B base) {
        CompoundTag root = new CompoundTag();

        root.put("0", StringTag.valueOf(obj.serializer().getKey().toString()));
        root.put("1", obj.serializer().get().toTagUnsafe(obj, base));

        return root;
    }

    public static <T> T unpackDirectly(Tag tag, Serializer<T> serializer) {
        return serializer.fromTag(tag);
    }

    public static <T, B> T unpackDirectly(Tag tag, B base, ContextualSerializer<T, B> serializer) {
        return serializer.fromTag(tag, base);
    }

    public static <T> T unpack(Tag tag, T defaultValue) {
        if (tag instanceof CompoundTag map && map.contains("0") && map.contains("1")) {
            Serializer<T> value = (Serializer<T>)BCSerializers.REGISTRY.get().getValue(new ResourceLocation(map.get("0").getAsString()));

            if (value != null) return value.fromTag(map.get("1"));
        }

        return defaultValue;
    }

    public static <T, B> T unpack(Tag tag, T defaultValue, B base) {
        if (tag instanceof CompoundTag map && map.contains("0") && map.contains("1")) {
            ContextualSerializer<T, B> value = (ContextualSerializer<T, B>)BCContextualSerializers.REGISTRY.get().getValue(new ResourceLocation(map.get("0").getAsString()));

            if (value != null) return value.fromTag(map.get("1"));
        }

        return defaultValue;
    }
}
