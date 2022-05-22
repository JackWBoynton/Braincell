package bottomtextdanny.braincell.mod._mod.object_tables;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.mod._base.registry.BCLivingEntityBuilder;
import bottomtextdanny.braincell.mod._base.registry.BCMobBuilder;
import bottomtextdanny.braincell.mod._base.registry.EntityWrap;
import bottomtextdanny.braincell.mod._base.registry.managing.BCRegistry;
import bottomtextdanny.braincell.mod.rendering.BCBoatRenderer;
import bottomtextdanny.braincell.mod._base.registry.BCEntityBuilder;
import bottomtextdanny.braincell.mod.world.builtin_entities.BCBoat;
import net.minecraft.world.entity.*;

public class BraincellEntities {
    public static final BCRegistry<EntityType<?>> ENTRIES = new BCRegistry<>();

    //*\\*//*\\*//*\\MISC\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//
    public static final EntityWrap<EntityType<BCBoat>> BOAT =
            startE("boat", BCBoat::new)
                    .classification(MobCategory.MISC)
                    .dimensions(1.375F, 0.5625F)
                    .renderer(() -> BCBoatRenderer::new)
                    .build();

    //*\\*//*\\*//*\\MISC\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//

    private static <E extends Entity> BCEntityBuilder<E> startE(String entityId, EntityType.EntityFactory<E> factory) {
        BCEntityBuilder<E> builder = new BCEntityBuilder<>(ENTRIES, Braincell.DEFERRING_STATE);
        builder.declare(entityId, factory);
        return builder;
    }

    private static <E extends LivingEntity> BCLivingEntityBuilder<E> startLE(String entityId, EntityType.EntityFactory<E> factory) {
        BCLivingEntityBuilder<E> builder = new BCLivingEntityBuilder<>(ENTRIES, Braincell.DEFERRING_STATE);
        builder.declare(entityId, factory);
        return builder;
    }

    private static <E extends Mob> BCMobBuilder<E> startM(String entityId, EntityType.EntityFactory<E> factory) {
        BCMobBuilder<E> builder = new BCMobBuilder<E>(ENTRIES, Braincell.DEFERRING_STATE);
        builder.declare(entityId, factory);
        return builder;
    }
}
