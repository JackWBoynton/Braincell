package net.bottomtextdanny.braincell.mod._base.registry;

import net.bottomtextdanny.braincell.mod.world.builtin_items.BCSpawnEggItem;
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

    EntityWrap<EntityType<T>> build();
}
