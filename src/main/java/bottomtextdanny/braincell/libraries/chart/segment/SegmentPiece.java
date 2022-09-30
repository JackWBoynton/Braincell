package bottomtextdanny.braincell.libraries.chart.segment;

import bottomtextdanny.braincell.libraries.chart._base.BCCustomTerrainProvider;
import bottomtextdanny.braincell.libraries.chart.segment.Segment;
import bottomtextdanny.braincell.libraries.chart.segment.SegmentAdmin;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.tables.BCSerializers;
import bottomtextdanny.braincell.tables.BCStructureTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

import static bottomtextdanny.braincell.libraries.registry.Serializer.*;

public class SegmentPiece extends StructurePiece implements BCCustomTerrainProvider {
    public static final String TERRAFORMER_LABEL = "terraformer";
    public static final String SEGMENT_LABEL = "segment";
    public static final String LEVEL_POS_LABEL = "slevel_pos";
    public static final String ROTATION_LABEL = "srotation";
    protected Segment<?> segment;
    protected BlockPos levelPos;
    protected Rotation rotation;
    protected TerrainAdjustment adjustment = TerrainAdjustment.NONE;
    private boolean ran;

    protected SegmentPiece(int genDepth, BoundingBox box, BlockPos levelPos, Rotation rotation, Segment<?> segment) {
        super(BCStructureTypes.CHILD_SEGMENT.get(), genDepth, box);
        this.rotation = rotation;
        this.boundingBox = box;
        this.segment = segment;
        this.levelPos = levelPos;
    }

    public SegmentPiece(CompoundTag root) {
        super(BCStructureTypes.CHILD_SEGMENT.get(), root);
        this.segment = unpack(root.get(SEGMENT_LABEL), (Segment<?>) null, (SegmentAdmin) null);
        this.levelPos = unpackDirectly(root.get(LEVEL_POS_LABEL), BCSerializers.BLOCKPOS.get());
        this.rotation = unpackDirectly(root.get(ROTATION_LABEL), BCSerializers.ROTATION.get());
        this.adjustment = unpackDirectly(root.get(TERRAFORMER_LABEL), BCSerializers.TERRAIN_ADJUSTMENT.get());
    }

    public void setAdjustment(TerrainAdjustment adjustment) {
        this.adjustment = adjustment;
    }

    @Override
    public BoundingBox terrainBoundingBox() {
        return boundingBox;
    }

    @Override
    public TerrainAdjustment terrainAdjustment() {
        return adjustment;
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext contxt, CompoundTag tag) {
        if (segment != null) tag.put(SEGMENT_LABEL, pack(segment, segment.administrator));
        tag.put(LEVEL_POS_LABEL, packDirectly(levelPos, BCSerializers.BLOCKPOS.get()));
        tag.put(ROTATION_LABEL, packDirectly(rotation, BCSerializers.ROTATION.get()));
        tag.put(TERRAFORMER_LABEL, Serializer.packDirectly(adjustment, BCSerializers.TERRAIN_ADJUSTMENT.get()));
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager manager,
                            ChunkGenerator chunkGenerator, RandomSource random,
                            BoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        if (!ran && segment != null) {
            ran = true;
            segment.start(level, levelPos, rotation);
        }
    }
}
