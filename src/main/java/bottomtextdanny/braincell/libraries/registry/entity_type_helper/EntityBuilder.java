package bottomtextdanny.braincell.libraries.registry.entity_type_helper;

import bottomtextdanny.braincell.libraries._minor.entity.EntityKey;
import bottomtextdanny.braincell.libraries._minor.entity.EntityParams;
import bottomtextdanny.braincell.libraries.network.Connection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.function.Function;
import java.util.function.Supplier;

public interface EntityBuilder<T extends Entity> {

    EntityBuilder<T> declare(String entityId, EntityType.EntityFactory<T> factory);

    EntityBuilder<T> classification(MobCategory classification);

    EntityBuilder<T> dimensions(float width, float height);

    EntityBuilder<T> renderer(Supplier<? extends Function<?, ?>> renderer);

    EntityBuilder<T> attributes(Supplier<AttributeSupplier.Builder> attributeMap);

    EntityBuilder<T> spawn(SpawnPlacements.Type placement, Heightmap.Types heightMap, SpawnPlacements.SpawnPredicate<T> pred);

    EntityBuilder<T> egg(BCSpawnEggItem.Builder egg);

    <U> EntityBuilder<T> param(Supplier<EntityKey<U>> key, Supplier<U> value);

    default <U> EntityBuilder<T> clientParam(Supplier<EntityKey<U>> key, Supplier<U> value) {
        Connection.doClientSide(() -> param(key, value));
        return this;
    }

    EntityWrap<EntityType<T>> build();

    record Param<T>(Supplier<EntityKey<T>> key, Supplier<T> valueFactory) {

        public void append(EntityType<?> type) {
            EntityParams.add(type, key.get(), valueFactory.get());
        }
    }
}
