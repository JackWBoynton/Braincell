package bottomtextdanny.braincell.events;

import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;
import org.apache.commons.lang3.mutable.MutableInt;

public final class SerializerLookupBuildingEvent extends Event {
    private final ImmutableMap.Builder<ResourceLocation, DataSerializer<?>> unbuiltSerializerLookup;
    private final ImmutableBiMap.Builder<Integer, DataSerializer<?>> unbuiltIdLookupBuilder;
    private final MutableInt idCounter;

    public SerializerLookupBuildingEvent(
            ImmutableMap.Builder<ResourceLocation, DataSerializer<?>> serializerLookup,
            ImmutableBiMap.Builder<Integer, DataSerializer<?>> idLookupBuilder,
            MutableInt idCounter) {
        this.unbuiltSerializerLookup = serializerLookup;
        this.unbuiltIdLookupBuilder = idLookupBuilder;
        this.idCounter = idCounter;
    }

    public void addSerializer(DataSerializer<?> mark) {
        this.unbuiltSerializerLookup.put(mark.key(), mark);
        this.unbuiltIdLookupBuilder.put(this.idCounter.getAndIncrement(), mark);
    }
}
