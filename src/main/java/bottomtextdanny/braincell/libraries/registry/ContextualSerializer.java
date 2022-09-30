package bottomtextdanny.braincell.libraries.registry;

import net.minecraft.nbt.Tag;

import java.util.function.BiFunction;
import java.util.function.Function;

public record ContextualSerializer<T, B>(BiFunction<T, B, Tag> toTag, BiFunction<Tag, B, T> fromTag) {

    public Tag toTagUnsafe(Object obj, B base) {
        return toTag.apply((T)obj, base);
    }

    public Tag toTag(T obj, B base) {
        return toTag.apply(obj, base);
    }

    public T fromTag(Tag obj) {
        return fromTag.apply(obj, null);
    }

    public T fromTag(Tag obj, B base) {
        return fromTag.apply(obj, base);
    }
}
