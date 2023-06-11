package bottomtextdanny.braincell.mod._mod.client_sided;

import bottomtextdanny.braincell.mod._base.AbstractModSide;
import bottomtextdanny.braincell.mod._base.animation.AnimationManager;
import bottomtextdanny.braincell.mod.capability.CapabilityHelper;
import bottomtextdanny.braincell.mod.capability.level.BCLevelCapability;
import bottomtextdanny.braincell.mod.capability.level.SpeckManagerModule;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@OnlyIn(Dist.CLIENT)
public final class BCClientSide extends AbstractModSide {
   public final Logger logger;
   private final ResourceLocation textureSink = new ResourceLocation("braincell", "textures/awesome.png");
   private final Int2ObjectMap directComms = new Int2ObjectOpenHashMap();
   private final ModelLoaderHandler extraModelLoaders;
   private final ParticleFactoryDeferror particleFactoryDeferror;
   private final BackendRenderingHandler renderingHandler;
   private ShaderHandler shaderHandler;
   private final EntityRendererDeferror entityRendererDeferror;
   private final AnimationManager animationManager;
   private final MaterialManager materialManager;
   private BraincellClientConfig config;
   @Nullable
   public ItemEntity hackyItemEntityTracker;

   private BCClientSide(String modId) {
      super(modId);
      MinecraftForge.EVENT_BUS.addListener(this::tick);
      this.logger = LogManager.getLogger(modId + "(client content)");
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

   public void modLoadingCallOut() {
      this.extraModelLoaders.sendListeners();
      this.particleFactoryDeferror.sendListeners();
      this.entityRendererDeferror.sendListeners();
      this.materialManager.sendListeners();
      ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
      this.config = new BraincellClientConfig(configBuilder);
      ModLoadingContext.get().registerConfig(Type.CLIENT, configBuilder.build());
   }

   private void tick(TickEvent.ClientTickEvent event) {
      if (event.phase == Phase.END) {
         Minecraft minecraft = Minecraft.getInstance();
         if (minecraft.level != null) {
            BCLevelCapability capability = (BCLevelCapability)CapabilityHelper.get(minecraft.level, BCLevelCapability.TOKEN);
            SpeckManagerModule speckManager = capability.getSpeckManager();
            speckManager.tick();
         }

         this.getShaderHandler().postProcessingTick();
      }

   }

   public void setComms(int key, FriendlyByteBuf dataStream) {
      this.directComms.put(key, dataStream);
   }

   @Nullable
   public FriendlyByteBuf getCommsThenDelete(int key) {
      FriendlyByteBuf extract = null;
      if (this.directComms.containsKey(key)) {
         extract = (FriendlyByteBuf)this.directComms.get(key);
         this.directComms.remove(key);
      }

      return extract;
   }

   public ResourceLocation sink() {
      return this.textureSink;
   }

   public void postModLoadingPhaseCallOut() {
   }

   public BraincellClientConfig config() {
      return this.config;
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
      return this.shaderHandler == null ? (this.shaderHandler = new ShaderHandler()) : this.shaderHandler;
   }

   public void resetShaderHandler() {
      this.shaderHandler = new ShaderHandler();
   }

   public EntityRendererDeferror getEntityRendererDeferror() {
      return this.entityRendererDeferror;
   }

   public AnimationManager getAnimationManager() {
      return this.animationManager;
   }

   public MaterialManager getMaterialManager() {
      return this.materialManager;
   }
}
