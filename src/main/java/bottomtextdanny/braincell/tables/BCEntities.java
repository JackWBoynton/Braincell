package bottomtextdanny.braincell.tables;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.registry.entity_type_helper.BCLivingEntityBuilder;
import bottomtextdanny.braincell.libraries.registry.entity_type_helper.BCMobBuilder;
import bottomtextdanny.braincell.libraries.registry.entity_type_helper.EntityWrap;
import bottomtextdanny.braincell.libraries.registry.BCRegistry;
import bottomtextdanny.braincell.libraries._minor.boat.client.BCBoatRenderer;
import bottomtextdanny.braincell.libraries.registry.entity_type_helper.BCEntityBuilder;
import bottomtextdanny.braincell.libraries._minor.boat.BCBoat;
import bottomtextdanny.braincell.test.IKEntity;
import bottomtextdanny.braincell.test.IKRenderer;
import net.minecraft.world.entity.*;

public final class BCEntities {
    public static final BCRegistry<EntityType<?>> ENTRIES = new BCRegistry<>();

    //*\\*//*\\*//*\\MISC\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//*\\*//
    public static final EntityWrap<EntityType<BCBoat>> BOAT =
            startE("boat", BCBoat::new)
                .classification(MobCategory.MISC)
                .dimensions(1.375F, 0.5625F)
                .renderer(() -> BCBoatRenderer::new)
                .build();

    public static final EntityWrap<EntityType<IKEntity>> IK =
        startE("ik", IKEntity::new)
            .classification(MobCategory.MISC)
            .dimensions(4.0F, 4.0F)
            .renderer(() -> IKRenderer::new)
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
