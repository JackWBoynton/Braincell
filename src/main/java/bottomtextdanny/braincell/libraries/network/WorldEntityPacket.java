package bottomtextdanny.braincell.libraries.network;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public interface WorldEntityPacket<T extends WorldEntityPacket<T, E>, E extends Entity> extends BCPacket<T> {

    int getEntityId();

    @SuppressWarnings("unchecked")
    default E getEntityAsReceptor(Level world) {
        if (world == null)
            throw new NullPointerException("entity-specialized packet provided with a null world reference.");
        return (E) world.getEntity(getEntityId());
    }
}
