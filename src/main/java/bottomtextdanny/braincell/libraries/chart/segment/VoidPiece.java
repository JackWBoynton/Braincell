package bottomtextdanny.braincell.libraries.chart.segment;

import bottomtextdanny.braincell.libraries.chart._base.BCCustomTerrainProvider;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.tables.BCSerializers;
import bottomtextdanny.braincell.tables.BCStructureTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public class VoidPiece extends StructurePiece implements BCCustomTerrainProvider {
    public static final String TERRAFORMER_LABEL = "terraformer";
    private final TerrainAdjustment adjustment;

    protected VoidPiece(BoundingBox p_209996_, TerrainAdjustment adjustment) {
        super(BCStructureTypes.VOID.get(), 0, p_209996_);
        this.adjustment = adjustment;
    }

    public VoidPiece(CompoundTag root) {
        super(BCStructureTypes.VOID.get(), root);
        this.adjustment = Serializer.unpackDirectly(root.get(TERRAFORMER_LABEL), BCSerializers.TERRAIN_ADJUSTMENT.get());
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.put(TERRAFORMER_LABEL, Serializer.packDirectly(adjustment, BCSerializers.TERRAIN_ADJUSTMENT.get()));
    }

    @Override
    public void postProcess(WorldGenLevel p_226769_, StructureManager p_226770_, ChunkGenerator p_226771_, RandomSource p_226772_, BoundingBox p_226773_, ChunkPos p_226774_, BlockPos p_226775_) {
    }

    @Override
    public BoundingBox terrainBoundingBox() {
        return boundingBox;
    }

    @Override
    public TerrainAdjustment terrainAdjustment() {
        return adjustment;
    }
}
