package bottomtextdanny.braincell;

import bottomtextdanny.braincell.mod._base.registry.managing.DeferrorType;
import bottomtextdanny.braincell.mod.capability.player.accessory.MiniAttribute;
import bottomtextdanny.braincell.mod.hooks.*;
import bottomtextdanny.braincell.mod.network.Connection;
import bottomtextdanny.braincell.mod.network.OnlyHandledOnClient;
import bottomtextdanny.braincell.mod._base.BCEvaluations;
import bottomtextdanny.braincell.mod.hooks.*;
import bottomtextdanny.braincell.mod._base.registry.managing.ModDeferringManager;
import bottomtextdanny.braincell.mod._mod.client_sided.BCClientSide;
import bottomtextdanny.braincell.mod._mod.common_sided.BCCommonSide;
import bottomtextdanny.braincell.mod._mod.object_tables.BraincellEntities;
import bottomtextdanny.braincell.mod._mod.object_tables.BraincellRecipes;
import bottomtextdanny.braincell.mod.capability.player.accessory.BCAccessoryKeys;
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
//                    new Vec3(1.0F, 0.0F, 0.0F), 5.0F, 1.3F, 0.8F);
//
//            client().getShaderHandler().getLightingWorkflow().addLight(light);
//        });

//        forgeEventBus.addListener((TickEvent.ClientTickEvent event) -> {
//            if (event.phase == TickEvent.Phase.END) return;
//
//            LocalPlayer player = Minecraft.getInstance().player;
//
//            if (player == null) return;
//
//            if (Minecraft.getInstance().options.keyDrop.isDown()) {
//                String glsl = GL30.glGetString(GL30.GL_SHADING_LANGUAGE_VERSION);
//
//                if (glsl != null) player.chat(glsl);
//                client().resetShaderHandler();
//
//                PointLightSpeck light = new ShootLighSpeck(player.level, 10, 10, 10);
//                light.setLight(new SimplePointLight(player.position(), new Vec3(1, 0, 0), 5, 1, 1));
//                light.addToLevel();
//            }
//        });
        modEventBus.addListener(Braincell::commonSetupPhaseHook);
        common.modLoadingCallOut();
        Connection.doClientSide(() -> {
            modEventBus.addListener(Braincell::clientSetupPhaseHook);
            ((BCClientSide)client).modLoadingCallOut();
        });
        BCAccessoryKeys.loadClass();
        BCEvaluations.loadClass();
        MiniAttribute.loadClass();
        DEFERRING_STATE.addRegistryDeferror(DeferrorType.RECIPE_SERIALIZER, BraincellRecipes.ENTRIES);
        DEFERRING_STATE.addRegistryDeferror(DeferrorType.ENTITY_TYPE, BraincellEntities.ENTRIES);
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
