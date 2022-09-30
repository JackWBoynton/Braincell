package bottomtextdanny.braincell;

import bottomtextdanny.braincell.tables.*;
import bottomtextdanny.braincell.libraries.network.Connection;
import bottomtextdanny.braincell.libraries.network.OnlyHandledOnClient;
import bottomtextdanny.braincell.libraries.mod.BCEvaluations;
import bottomtextdanny.braincell.libraries.registry.ModDeferringManager;
import bottomtextdanny.braincell.interaction.*;
import net.minecraft.core.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static bottomtextdanny.braincell.Braincell.ID;

@Mod(ID)
public class Braincell {
    public static final String NAME = "Braincell";
    public static final String ID = "braincell";
    public static final Logger LOGGER = LogManager.getLogger(ID);
    private static final BCCommonSide common = BCCommonSide.with(ID);
    @OnlyHandledOnClient
    private static final Object client = Connection.makeClientSideUnknown(() -> BCClientSide.with(ID));
    public static final ModDeferringManager DEFERRING_STATE = new ModDeferringManager(ID);

    public Braincell() {
        super();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        forgeEventBus.addListener(BCAttackHooks::criticalHitHook);
        forgeEventBus.addListener(BCAttackHooks::damageLivingHook);
        forgeEventBus.addListener(BCConnectionHooks::trackEntityHook);
        forgeEventBus.addListener(BCDeathHooks::livingDeathHook);
        forgeEventBus.addListener(BCHurtHooks::livingHurtHook);
        forgeEventBus.addListener(BCMovementHooks::livingFallHook);
        forgeEventBus.addListener(BCMovementHooks::livingJumpHook);
        forgeEventBus.addListener(BCTickHooks::serverLevelTickHook);
        forgeEventBus.addListener(BCTickHooks::playerTickHook);

//        forgeEventBus.addListener((RenderLivingEvent.Post<?, ?> event) -> {
//            if (event.getEntity() == null) return;
//            SimplePointLight light = new SimplePointLight(event.getEntity().position(),
//                    new Vec3(1, 0.8, .1), 5.0F, 0.8F, 1.8F);
//
//            client().getShaderHandler().getLightingWorkflow().addLight(light);
//        });
//
//        forgeEventBus.addListener((TickEvent.ClientTickEvent event) -> {
//            if (event.phase == TickEvent.Phase.END) return;
//
//            LocalPlayer player = Minecraft.getInstance().player;
//
//            if (player == null) return;
//
//            if (Minecraft.getInstance().options.keyDrop.isDown()) {
//
//                player.chat(String.valueOf(BCContextualSerializers.REGISTRY.get() != null));
//                client().resetShaderHandler();
//
//                PointLightSpeck light = new ShootLightSpeck(player.level, 10, 10, 10);
//                light.setLight(new SimplePointLight(player.position(), new Vec3(1, 0.8, .1), 5, 1, 1));
//                light.addToLevel();
//            }
//        });
        modEventBus.addListener(Braincell::commonSetupPhaseHook);
        common.modLoadingCallOut();
        Connection.doClientSide(() -> {
            modEventBus.addListener(Braincell::clientSetupPhaseHook);
            ((BCClientSide)client).modLoadingCallOut();
        });
        BCStepDataTypes.registerRegistry();
        BCCharts.registerRegistry();
        BCSerializers.registerRegistry();
        BCContextualSerializers.registerRegistry();
        BCAccessoryKeys.registerRegistry();
        BCAccessoryAttributeModifiers.registerRegistry();
        BCEvaluations.loadClass();
        BCEntityKeys.registerRegistry();
        DEFERRING_STATE.addRegistryDeferror(Registry.ATTRIBUTE_REGISTRY, BCAttributes.ENTRIES);
        DEFERRING_STATE.addRegistryDeferror(BCAccessoryKeys.REGISTRY_KEY, BCAccessoryKeys.ENTRIES);
        DEFERRING_STATE.addRegistryDeferror(BCAccessoryAttributeModifiers.REGISTRY_KEY, BCAccessoryAttributeModifiers.ENTRIES);
        DEFERRING_STATE.addRegistryDeferror(BCStepDataTypes.REGISTRY_KEY, BCStepDataTypes.ENTRIES);
        DEFERRING_STATE.addRegistryDeferror(BCCharts.REGISTRY_KEY, BCCharts.ENTRIES);
        DEFERRING_STATE.addRegistryDeferror(BCSerializers.REGISTRY_KEY, BCSerializers.ENTRIES);
        DEFERRING_STATE.addRegistryDeferror(BCContextualSerializers.REGISTRY_KEY, BCContextualSerializers.ENTRIES);
        DEFERRING_STATE.addRegistryDeferror(Registry.STRUCTURE_PIECE_REGISTRY, BCStructureTypes.ENTRIES);
        DEFERRING_STATE.addRegistryDeferror(Registry.RECIPE_SERIALIZER_REGISTRY, BCRecipes.ENTRIES);
        DEFERRING_STATE.addRegistryDeferror(BCEntityKeys.REGISTRY_KEY, BCEntityKeys.ENTRIES);
        DEFERRING_STATE.addRegistryDeferror(Registry.BLOCK_REGISTRY, BCBlocks.ENTRIES);
        DEFERRING_STATE.addRegistryDeferror(Registry.ITEM_REGISTRY, BCItems.ENTRIES);
        DEFERRING_STATE.addRegistryDeferror(Registry.BLOCK_ENTITY_TYPE_REGISTRY, BCBlockEntities.ENTRIES);
        DEFERRING_STATE.addRegistryDeferror(Registry.ENTITY_TYPE_REGISTRY, BCEntities.ENTRIES);
        DEFERRING_STATE.solveAndLockForeverEver();
    }

    public static void commonSetupPhaseHook(FMLCommonSetupEvent event) {
        common.postModLoadingPhaseCallOut();
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientSetupPhaseHook(FMLClientSetupEvent event) {
        ((BCClientSide)client).postModLoadingPhaseCallOut();
    }

    public static BCCommonSide common() {
        return common;
    }

    @OnlyIn(Dist.CLIENT)
    public static BCClientSide client() {
        return (BCClientSide)client;
    }
}
