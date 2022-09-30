package bottomtextdanny.braincell.libraries.chart._base;

import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;

public interface BCCustomTerrainProvider {

    BoundingBox terrainBoundingBox();

    TerrainAdjustment terrainAdjustment();
}
