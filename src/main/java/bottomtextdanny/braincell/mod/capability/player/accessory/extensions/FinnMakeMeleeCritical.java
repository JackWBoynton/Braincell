package bottomtextdanny.braincell.mod.capability.player.accessory.extensions;

import net.minecraft.world.entity.LivingEntity;
import org.apache.commons.lang3.mutable.MutableFloat;

public interface FinnMakeMeleeCritical {
    int MAKE_CRITICAL_PRIORITY_HIGH = 0, MAKE_CRITICAL_PRIORITY_LOW = 1;

    int critMakingModulePriority();

    void makeMeleeCritical(LivingEntity entity, MutableFloat chance, CriticalStateMutable directState);
}
