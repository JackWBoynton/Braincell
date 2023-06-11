package bottomtextdanny.braincell.mod._mod.client_sided;

import net.minecraftforge.common.ForgeConfigSpec;

public final class BraincellClientConfig {
   private final ForgeConfigSpec.BooleanValue lights;
   private final ForgeConfigSpec.IntValue xTileDivisions;
   private final ForgeConfigSpec.IntValue yTileDivisions;
   private final ForgeConfigSpec.IntValue maxLights;
   private final ForgeConfigSpec.IntValue maxLightsPerTile;
   private final ForgeConfigSpec.BooleanValue screenTonemapping;

   public BraincellClientConfig(ForgeConfigSpec.Builder builder) {
      builder.push("Shader Lights");
      this.lights = builder.define("process_and_render_lights", true);
      this.xTileDivisions = builder.defineInRange("x_tile_divisions", 24, 4, 256);
      this.yTileDivisions = builder.defineInRange("y_tile_divisions", 16, 4, 256);
      this.maxLights = builder.defineInRange("max_lights", 256, 4, 1024);
      this.maxLightsPerTile = builder.defineInRange("max_lights_per_tile", 48, 4, 256);
      builder.pop();
      builder.push("Screen Tonemapping");
      this.screenTonemapping = builder.define("process_and_render_screen_tonemapping", true);
      builder.pop();
   }

   public boolean lights() {
      return (Boolean)this.lights.get();
   }

   public int xTileDivisions() {
      return (Integer)this.xTileDivisions.get();
   }

   public int yTileDivisions() {
      return (Integer)this.yTileDivisions.get();
   }

   public int maxLights() {
      return (Integer)this.maxLights.get();
   }

   public int maxLightsPerTile() {
      return (Integer)this.maxLightsPerTile.get();
   }

   public boolean screenTonemapping() {
      return (Boolean)this.screenTonemapping.get();
   }
}
