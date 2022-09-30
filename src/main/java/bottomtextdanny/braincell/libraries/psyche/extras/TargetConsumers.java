package bottomtextdanny.braincell.libraries.psyche.extras;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;

import java.util.function.*;

public final class TargetConsumers {

    public static <T extends Entity> BiConsumer<Mob, LivingEntity> entityGroupAction(Class<T> searchClass,
                                                                                     Function<Mob, AABB> searchBox,
                                                                                     TargetSideReaction<? super T> action) {
        return (mob, target) -> {
            if (mob.level instanceof ServerLevel serverLevel) {
                serverLevel.getEntities().get(EntityTypeTest.forClass(searchClass), searchBox.apply(mob), entity -> {
                    action.accept(entity, mob, target);
                });
            }
        };
    }

    private TargetConsumers() {}
}
