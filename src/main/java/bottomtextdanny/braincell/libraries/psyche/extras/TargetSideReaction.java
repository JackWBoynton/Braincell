package bottomtextdanny.braincell.libraries.psyche.extras;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

@FunctionalInterface
public interface TargetSideReaction<T> {

    void accept(T entity, Mob targeter, LivingEntity target);
}
