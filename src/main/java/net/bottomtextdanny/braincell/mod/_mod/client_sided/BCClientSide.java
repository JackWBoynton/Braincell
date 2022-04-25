package net.bottomtextdanny.braincell.mod._mod.client_sided;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.bottomtextdanny.braincell.Braincell;
import net.bottomtextdanny.braincell.mod._base.AbstractModSide;
import net.bottomtextdanny.braincell.mod._base.animation.AnimationManager;
import net.bottomtextdanny.braincell.mod._base.capability.CapabilityHelper;
import net.bottomtextdanny.braincell.mod.capability.level.BCLevelCapability;
import net.bottomtextdanny.braincell.mod.capability.level.SpeckManagerModule;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public final class BCClientSide extends AbstractModSide {
    public final Logger logger;
    private final ResourceLocation textureSink = new ResourceLocation(Braincell.ID, "textures/awesome.png");
    private final Int2ObjectMap<FriendlyByteBuf> directComms = new Int2ObjectOpenHashMap<>();
    private final ModelLoaderHandler extraModelLoaders;
    private final ParticleFactoryDeferror particleFactoryDeferror;
    private final BackendRenderingHandler renderingHandler;
    private ShaderHandler postprocessingHandler;
    private final EntityRendererDeferror entityRendererDeferror;
    private final AnimationManager animationManager;
    private final MaterialManager materialManager;

    private BCClientSide(String modId) {
        super(modId);
        MinecraftForge.EVENT_BUS.addListener(this::tick);
        this.logger = LogManager.getLogger(String.join(modId, "(client content)"));
        this.extraModelLoaders = new ModelLoaderHandler();
        this.particleFactoryDeferror = new ParticleFactoryDeferror();
        this.renderingHandler = new BackendRenderingHandler();
        this.entityRendererDeferror = new EntityRendererDeferror();
        this.animationManager = new AnimationManager();
        this.materialManager = new MaterialManager();
    }

    public static BCClientSide with(String modId) {
        return new BCClientSide(modId);
    }

    private void tick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (Minecraft.getInstance().level != null) {
                BCLevelCapability capability = CapabilityHelper.get(Minecraft.getInstance().level, BCLevelCapability.TOKEN);
                SpeckManagerModule speckManager = capability.getSpeckManager();
                speckManager.tick();
            }
            getShaderHandler().postProcessingTick();
        }
    }

    @Override
    public void modLoadingCallOut() {
        this.extraModelLoaders.sendListeners();
        this.particleFactoryDeferror.sendListeners();
        this.entityRendererDeferror.sendListeners();
        this.materialManager.sendListeners();
    }

    public void setComms(int key, FriendlyByteBuf dataStream) {
        this.directComms.put(key, dataStream);
    }

    @Nullable
    public FriendlyByteBuf getCommsThenDelete(int key) {
        FriendlyByteBuf extract = null;
        if (this.directComms.containsKey(key)) {
            extract = this.directComms.get(key);
            this.directComms.remove(key);
        }
        return extract;
    }

    public ResourceLocation sink() {
        return this.textureSink;
    }

    @Override
    public void postModLoadingPhaseCallOut() {
    }

    public ModelLoaderHandler getExtraModelLoaders() {
        return this.extraModelLoaders;
    }

    public ParticleFactoryDeferror getParticleFactoryDeferror() {
        return this.particleFactoryDeferror;
    }

    public BackendRenderingHandler getRenderingHandler() {
        return this.renderingHandler;
    }

    public ShaderHandler getShaderHandler() {
        return this.postprocessingHandler == null ? (this.postprocessingHandler = new ShaderHandler()) : this.postprocessingHandler;
    }

    public EntityRendererDeferror getEntityRendererDeferror() {
        return this.entityRendererDeferror;
    }

    public AnimationManager getAnimationManager() {
        return animationManager;
    }

    public MaterialManager getMaterialManager() {
        return this.materialManager;
    }
}
