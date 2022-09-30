package bottomtextdanny.braincell.libraries.registry;

import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public final class ItemSortData implements AutoCloseable {
    private final Map<ResourceLocation, Short> sortValueMap = Maps.newHashMapWithExpectedSize(256);

    public ItemSortData() {
        super();
    }

    public void setSortValue(ResourceLocation key, short sortValue) {
        this.sortValueMap.put(key, sortValue);
    }

    public short getSortValue(ResourceLocation key) {
        if (!this.sortValueMap.containsKey(key)) return (short) -1;
        else return this.sortValueMap.get(key);
    }

    @Override
    public void close() {
        this.sortValueMap.clear();
    }
}
