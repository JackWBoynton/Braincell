package net.bottomtextdanny.braincell.mod._base.registry;

import net.bottomtextdanny.braincell.Braincell;
import net.bottomtextdanny.braincell.mod.network.Connection;
import net.bottomtextdanny.braincell.mod._base.registry.managing.BCRegistry;
import net.bottomtextdanny.braincell.mod._base.registry.managing.ModDeferringManager;
import net.bottomtextdanny.braincell.mod._mod.client_sided.EntityRendererDeferring;
import net.bottomtextdanny.braincell.mod._mod.client_sided.EntityRendererMaker;
import net.bottomtextdanny.braincell.mod._mod.common_sided.EntityAttributeDeferror;
import net.bottomtextdanny.braincell.mod.world.builtin_items.BCSpawnEggItem;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.function.Function;
import java.util.function.Supplier;

public class BCLivingEntityBuilder<T extends LivingEntity> implements EntityBuilder<T> {
    private final ModDeferringManager solving;
    private final BCRegistry<EntityType<?>> registry;
    protected String entityId;
    protected EntityType.EntityFactory<T> factory;
    protected MobCategory classification;
    protected Supplier<AttributeSupplier.Builder> attributeMap;
    protected float width, height;
    protected Supplier<?> renderFactory;

    public BCLivingEntityBuilder(BCRegistry<EntityType<?>> registry, ModDeferringManager solving) {
        super();
        this.solving = solving;
        this.registry = registry;
    }

    @Override
    public BCLivingEntityBuilder<T> declare(String entityId, EntityType.EntityFactory<T> factory) {
        this.entityId = entityId;
        this.factory = factory;

        return this;
    }

    @Override
    public BCLivingEntityBuilder<T> classification(MobCategory classification) {
        this.classification = classification;

        return this;
    }

    @Override
    public BCLivingEntityBuilder<T> dimensions(float width, float height) {
        this.width = width;
        this.height = height;

        return this;
    }

    @Override
    public BCLivingEntityBuilder<T> renderer(Supplier<? extends Function<?,?>> renderer) {
        Connection.doClientSide(() -> {
            this.renderFactory = () -> EntityRendererMaker.makeOf(renderer.get());
        });
        return this;
    }

    @Override
    public BCLivingEntityBuilder<T> attributes(Supplier<AttributeSupplier.Builder> attributeMap) {
        this.attributeMap = attributeMap;
        return this;
    }

    @Override
    @Deprecated
    public BCLivingEntityBuilder<T> spawn(SpawnPlacements.Type placement, Heightmap.Types heightMap, SpawnPlacements.SpawnPredicate<T> pred) {
        throw new UnsupportedOperationException("cannot configure spawn of a non-mob entity type");
    }

    @Override
    @Deprecated
    public BCLivingEntityBuilder<T> egg(BCSpawnEggItem.Builder egg) {
		throw new UnsupportedOperationException("cannot infer egg on a non-mob entity type");
    }

    @Override
    public EntityWrap<EntityType<T>> build() {
        EntityWrap<EntityType<T>> type = new EntityWrap(new ResourceLocation(this.solving.getModID(), this.entityId), () -> EntityType.Builder.of(this.factory, this.classification).sized(this.width, this.height).build(this.solving.getModID() + ":" + this.entityId));
        type.setupForDeferring(this.solving);
        this.registry.addDeferredSolving(type);

        if (this.attributeMap != null) {
            Braincell.common().getEntityCoreDataDeferror()
                    .deferAttributeAttachment(new EntityAttributeDeferror(type, this.attributeMap));
        }
        Connection.doClientSide(() -> {
            if (this.renderFactory != null) {
                Braincell.client().getEntityRendererDeferror().add(new EntityRendererDeferring<>(type, (Supplier<EntityRendererProvider<T>>)this.renderFactory));
            }
        });
        return type;
    }
}
