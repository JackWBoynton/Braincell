package bottomtextdanny.braincell.libraries._minor.entity_data_manager.serialization;

import bottomtextdanny.braincell.libraries.serialization.DataSerializer;
import net.minecraft.world.entity.Entity;

import java.util.function.Supplier;

public record EntityDataReference<T>(DataSerializer<T> serializer, Supplier<T> defaultProvider, String storageKey, Class<? extends Entity> accessor, int lookupMark) {}
