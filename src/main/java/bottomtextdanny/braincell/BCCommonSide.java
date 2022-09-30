package bottomtextdanny.braincell;

import bottomtextdanny.braincell.interaction.BCCapabilityHooks;
import bottomtextdanny.braincell.libraries._minor.chest.ChestOverriderManager;
import bottomtextdanny.braincell.libraries.registry.ItemSortData;
import bottomtextdanny.braincell.libraries.serialization.SerializerLookup;
import bottomtextdanny.braincell.libraries.mod.AbstractModSide;
import bottomtextdanny.braincell.libraries.accessory.AccessoryKey;
import bottomtextdanny.braincell.libraries._minor.MSGComms;
import bottomtextdanny.braincell.libraries.registry.DefaultSolvingHooks;
import bottomtextdanny.braincell.libraries.registry.entity_type_helper.EntityCoreDataDeferror;
import bottomtextdanny.braincell.tables.BCAttributes;
import com.google.common.collect.ImmutableSet;
import bottomtextdanny.braincell.libraries.chart.schema.PropertyMap;
import bottomtextdanny.braincell.libraries.chart.schema.SchemaManager;
import bottomtextdanny.braincell.libraries._minor.entity.EntityHurtCallout;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class BCCommonSide extends AbstractModSide {
    public final Logger logger;
    private final SchemaManager schemaManager;
    private final DefaultSolvingHooks solvingHooks;
    private final EntityCoreDataDeferror entityCoreDataDeferror;
    private final ChestOverriderManager chestOverriderManager;
    private final ItemSortData itemSortData;
    private SerializerLookup serializerLookUp;
    public final ImmutableSet<String> loadedMods;
    private boolean passedInitialization;

    public BCCommonSide(String modId) {
        super(modId);
        this.loadedMods = ImmutableSet.copyOf(FMLLoader.getLoadingModList().getMods()
                .stream()
                .map(ModInfo::getModId)
                .toArray(String[]::new));
        this.logger = LogManager.getLogger(modId + "(common content)");
        BCPacketInitialization.initializeNetworkPackets();
        this.schemaManager = new SchemaManager();
        this.serializerLookUp = SerializerLookup.createUnbuilt();
        this.solvingHooks = new DefaultSolvingHooks();
        this.itemSortData = new ItemSortData();
        this.chestOverriderManager = new ChestOverriderManager();
        this.entityCoreDataDeferror = new EntityCoreDataDeferror();
    }

    public boolean isModThere(String modId) {
        return this.loadedMods.contains(modId);
    }

    public static BCCommonSide with(String modId) {
        return new BCCommonSide(modId);
    }

    @Override
    public void modLoadingCallOut() {
        this.entityCoreDataDeferror.sendListeners();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        modEventBus.addListener(BCCapabilityHooks::register);
        forgeEventBus.addGenericListener(Level.class, BCCapabilityHooks::attachToLevel);
        forgeEventBus.addGenericListener(Entity.class, BCCapabilityHooks::attachToEntity);
        modEventBus.addListener((EntityAttributeModificationEvent event) -> {
            event.add(EntityType.PLAYER, BCAttributes.CRITICAL_DAMAGE.get());
            event.add(EntityType.PLAYER, BCAttributes.CRITICAL_CHANCE.get());
            event.add(EntityType.PLAYER, BCAttributes.ARCHERY_DAMAGE_MULTIPLIER.get());
            event.add(EntityType.PLAYER, BCAttributes.ARROW_SPEED_MULTIPLIER.get());
        });
    }

    @Override
    public void postModLoadingPhaseCallOut() {
        PropertyMap.processClass("mc", BlockStateProperties.class);
        this.passedInitialization = true;
        this.serializerLookUp = ((SerializerLookup.NotBuilt)this.serializerLookUp).build();

        if (BraincellModules.ENTITY_HURT_CALL_OUT.isActive()) {
            MinecraftForge.EVENT_BUS.addListener((LivingHurtEvent event) -> {
                if (event.getEntity() instanceof EntityHurtCallout) {
                    event.setAmount(((EntityHurtCallout) event.getEntity()).hurtCallOut(event.getAmount(), event.getSource()));
                }
            });
        }
    }

    public void sendComms(ServerPlayer player, int id, FriendlyByteBuf dataStream) {
        new MSGComms(id, dataStream).sendTo(PacketDistributor.PLAYER.with(() -> player));
    }

    public boolean hasPassedInitialization() {
        return this.passedInitialization;
    }

    public SchemaManager getSchemaManager() {
        return schemaManager;
    }

    public DefaultSolvingHooks getSolvingHooks() {
        return this.solvingHooks;
    }

    public ChestOverriderManager getChestOverriderManager() {
        return this.chestOverriderManager;
    }

    public EntityCoreDataDeferror getEntityCoreDataDeferror() {
        return this.entityCoreDataDeferror;
    }

    public ItemSortData getItemSortData() {
        return this.itemSortData;
    }

    public SerializerLookup getSerializerLookUp() {
        return this.serializerLookUp;
    }
}
