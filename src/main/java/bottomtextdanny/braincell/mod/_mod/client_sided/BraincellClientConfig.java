package bottomtextdanny.braincell.mod._mod.client_sided;

import bottomtextdanny.braincell.Braincell;
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
        lights = builder.define("process_and_render_lights", true);
        xTileDivisions = builder.defineInRange("x_tile_divisions", 24, 4, 256);
        yTileDivisions = builder.defineInRange("y_tile_divisions", 16, 4, 256);
        maxLights = builder.defineInRange("max_lights", 256, 4, 1024);
        maxLightsPerTile = builder.defineInRange("max_lights_per_tile", 48, 4, 256);
        builder.pop();

        builder.push("Screen Tonemapping");
        screenTonemapping = builder.define("process_and_render_screen_tonemapping", true);
        builder.pop();
    }

    //shader lights

    public boolean lights() {
        return lights.get();
    }

    public int xTileDivisions() {
        return xTileDivisions.get();
    }

    public int yTileDivisions() {
        return yTileDivisions.get();
    }

    public int maxLights() {
        return maxLights.get();
    }

    public int maxLightsPerTile() {
        return maxLightsPerTile.get();
    }

    //screen tonemapping

    public boolean screenTonemapping() {
        return screenTonemapping.get();
    }
}
