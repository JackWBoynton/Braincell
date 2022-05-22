package bottomtextdanny.braincell.mod.entity.psyche.extras;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

@FunctionalInterface
public interface TargetSideReaction<T> {

    void accept(T entity, Mob targeter, LivingEntity target);
}
