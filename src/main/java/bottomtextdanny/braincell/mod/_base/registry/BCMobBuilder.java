package bottomtextdanny.braincell.mod._base.registry;

import bottomtextdanny.braincell.mod.network.Connection;
import bottomtextdanny.braincell.mod.world.builtin_items.BCSpawnEggItem;
import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.mod._base.registry.managing.BCRegistry;
import bottomtextdanny.braincell.mod._base.registry.managing.ModDeferringManager;
import bottomtextdanny.braincell.mod._mod.client_sided.EntityRendererDeferring;
import bottomtextdanny.braincell.mod._mod.client_sided.EntityRendererMaker;
import bottomtextdanny.braincell.mod._mod.common_sided.EntityAttributeDeferror;
import bottomtextdanny.braincell.mod._mod.common_sided.EntityCoreDataDeferror;
import bottomtextdanny.braincell.mod._mod.common_sided.RawEntitySpawnDeferring;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Function;
import java.util.function.Supplier;

public class BCMobBuilder<T extends Mob> implements EntityBuilder<T> {
    private final ModDeferringManager solving;
    private final BCRegistry<EntityType<?>> registry;
	private RawEntitySpawnDeferring<T> spawn;
    protected String entityId;
    protected EntityType.EntityFactory<T> factory;
    protected MobCategory classification;
    protected Supplier<AttributeSupplier.Builder> attributeMap;
    protected float width, height;
    protected EggBuildData eggBuilder;
    @OnlyIn(Dist.CLIENT)
    protected Supplier<?> renderFactory;

    public BCMobBuilder(BCRegistry<EntityType<?>> registry, ModDeferringManager solving) {
        super();
        this.solving = solving;
        this.registry = registry;
    }

    @Override
    public BCMobBuilder<T> declare(String entityId, EntityType.EntityFactory<T> factory) {
        this.entityId = entityId;
        this.factory = factory;

        return this;
    }

    @Override
    public BCMobBuilder<T> classification(MobCategory classification) {
        this.classification = classification;

        return this;
    }

    @Override
    public BCMobBuilder<T> dimensions(float width, float height) {
        this.width = width;
        this.height = height;

        return this;
    }

    @Override
    public BCMobBuilder<T> renderer(Supplier<? extends Function<?, ?>> renderer) {
        Connection.doClientSide(() -> {
            this.renderFactory = () -> EntityRendererMaker.makeOf(renderer.get());
        });

        return this;
    }

    @Override
    public BCMobBuilder<T> attributes(Supplier<AttributeSupplier.Builder> attributeMap) {
        this.attributeMap = attributeMap;
        return this;
    }

    @Override
    public BCMobBuilder<T> spawn(SpawnPlacements.Type placement, Heightmap.Types heightMap, SpawnPlacements.SpawnPredicate<T> pred) {
        this.spawn = new RawEntitySpawnDeferring<>(placement, heightMap, pred);
        return this;
    }

    public BCMobBuilder<T> egg(String name, BCSpawnEggItem.Builder egg) {
        this.eggBuilder = new EggBuildData(name, egg);
        return this;
    }

    @Override
    public BCMobBuilder<T> egg(BCSpawnEggItem.Builder egg) {
        return egg(this.entityId, egg);
    }
	
	@Override
	public EntityWrap<EntityType<T>> build() {
        EntityCoreDataDeferror deferrorCore = Braincell.common().getEntityCoreDataDeferror();
		EntityWrap<EntityType<T>> type = new EntityWrap<>(new ResourceLocation(this.solving.getModID(), this.entityId), () -> EntityType.Builder.of(this.factory, this.classification).sized(this.width, this.height).build(this.solving.getModID() + ":" + this.entityId));
		type.setupForDeferring(this.solving);
		this.registry.addDeferredSolving(type);

		if (this.eggBuilder != null) {
            deferrorCore.saveEggBuilder(type.getKey(), this.eggBuilder);
		}

		if (this.attributeMap != null) {
            deferrorCore.deferAttributeAttachment(new EntityAttributeDeferror(type, this.attributeMap));
        }

		if (this.spawn != null) {
            deferrorCore.deferSpawnPlacement(this.spawn.makeDeferring(type));
		}

        Connection.doClientSide(() -> {
            if (this.renderFactory != null) {
                Braincell.client().getEntityRendererDeferror().add(
                        new EntityRendererDeferring<>(type, (Supplier<EntityRendererProvider<T>>)this.renderFactory));
            }
        });
		return type;
	}
}
