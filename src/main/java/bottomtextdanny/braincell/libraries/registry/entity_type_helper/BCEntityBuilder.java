package bottomtextdanny.braincell.libraries.registry.entity_type_helper;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries._minor.entity.EntityKey;
import bottomtextdanny.braincell.libraries.registry.BCRegistry;
import bottomtextdanny.braincell.libraries.registry.ModDeferringManager;
import bottomtextdanny.braincell.libraries.network.Connection;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class BCEntityBuilder<T extends Entity> implements EntityBuilder<T> {
    private final ModDeferringManager solving;
    private final BCRegistry<EntityType<?>> registry;
    protected String entityId;
    protected EntityType.EntityFactory<T> factory;
    protected MobCategory classification;
    protected float width, height;
    protected Supplier<?> renderFactory;
    protected List<Param<?>> parameters;

    public BCEntityBuilder(BCRegistry<EntityType<?>> registry, ModDeferringManager solving) {
        super();
        this.solving = solving;
        this.registry = registry;
        this.parameters = new LinkedList<>();
    }

    @Override
    public BCEntityBuilder<T> declare(String entityId, EntityType.EntityFactory<T> factory) {
        this.entityId = entityId;
        this.factory = factory;

        return this;
    }

    @Override
    public BCEntityBuilder<T> classification(MobCategory classification) {
        this.classification = classification;

        return this;
    }

    @Override
    public BCEntityBuilder<T> dimensions(float width, float height) {
        this.width = width;
        this.height = height;

        return this;
    }

    @Override
    public BCEntityBuilder<T> renderer(Supplier<? extends Function<?,?>> renderer) {
        Connection.doClientSide(() -> {
            this.renderFactory = () -> EntityRendererMaker.makeOf(renderer.get());
        });
        return this;
    }

    @Override
    @Deprecated
    public BCEntityBuilder<T> attributes(Supplier<AttributeSupplier.Builder> attributeMap) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public BCEntityBuilder<T> spawn(SpawnPlacements.Type placement, Heightmap.Types heightMap, SpawnPlacements.SpawnPredicate<T> pred) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public BCEntityBuilder<T> egg(BCSpawnEggItem.Builder egg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <U> EntityBuilder<T> param(Supplier<EntityKey<U>> key, Supplier<U> value) {
        parameters.add(new Param<>(key, value));
        return this;
    }

    @Override
    public EntityWrap<EntityType<T>> build() {
        EntityWrap<EntityType<T>> type = new EntityWrap<>(new ResourceLocation(this.solving.getModID(), this.entityId), () -> EntityType.Builder.of(this.factory, this.classification).sized(this.width, this.height).build(this.solving.getModID() + ":" + this.entityId));
        type.setupForDeferring(this.solving);
        this.registry.addDeferredSolving(type);

        Connection.doClientSide(() -> {
            if (this.renderFactory != null) {
                Braincell.client().getEntityRendererDeferror().add(new EntityRendererDeferring<>(type, (Supplier<EntityRendererProvider<T>>)this.renderFactory));
            }
        });

        Braincell.common().getEntityCoreDataDeferror().deferEntityParameters(new EntityParameterDeferror(type, parameters));

        return type;
    }
}
