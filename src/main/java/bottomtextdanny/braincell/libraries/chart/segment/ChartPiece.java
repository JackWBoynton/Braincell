package bottomtextdanny.braincell.libraries.chart.segment;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.libraries.chart._base.BCCustomTerrainProvider;
import bottomtextdanny.braincell.libraries.chart.help.BBox;
import bottomtextdanny.braincell.libraries.chart.segment_maker.SegmentPregeneration;
import bottomtextdanny.braincell.tables.BCSerializers;
import bottomtextdanny.braincell.tables.BCStructureTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import static bottomtextdanny.braincell.libraries.registry.Serializer.*;

public class ChartPiece extends StructurePiece implements BCCustomTerrainProvider {
    public static final String TERRAFORMER_LABEL = "terraformer";
    public static final String CHART_LABEL = "chart";
    public static final String ACTOR_LABEL = "actor";
    public static final String ARGUMENTS_LABEL = "args";
    public static final String ROTATION_LABEL = "srotation";
    public static final String LEVEL_POS_LABEL = "lvlpos";
    public static final String POS_PROCESSOR = "pprocess";
    protected BlockPos levelPos;
    protected final Chart chart;
    protected final int actor;
    private final ObjectFetcher arguments;
    protected Rotation rotation;
    protected TerrainAdjustment adjustment = TerrainAdjustment.NONE;
    private Segment<?> segment;
    private SegmentPregeneration posProcessor;
    private boolean ran;

    public ChartPiece(int genDepth, BoundingBox box, Rotation rotation, BlockPos levelPos,
                      Chart chart, int actor, Object... arguments) {
        super(BCStructureTypes.CHART.get(), genDepth, box);
        this.rotation = rotation;
        this.boundingBox = box;
        this.levelPos = levelPos;
        this.chart = chart;
        this.actor = actor;
        this.arguments = ObjectFetcher.of(arguments);
    }

    public ChartPiece(CompoundTag root) {
        super(BCStructureTypes.CHART.get(), root);
        this.rotation = unpackDirectly(root.get(ROTATION_LABEL), BCSerializers.ROTATION.get());
        this.adjustment = unpackDirectly(root.get(TERRAFORMER_LABEL), BCSerializers.TERRAIN_ADJUSTMENT.get());
        this.levelPos = unpackDirectly(root.get(LEVEL_POS_LABEL), BCSerializers.BLOCKPOS.get());
        this.chart = unpackDirectly(root.get(CHART_LABEL), BCSerializers.CHART.get());
        this.actor = root.getInt(ACTOR_LABEL);
        this.arguments = chart.deserialize(root.getList(ARGUMENTS_LABEL, 10));
        makeSegment();
    }

    public void makeSegment() {
        Piece piece = chart.actors().get(actor);
        this.posProcessor = piece.posProcessor();
        this.segment = piece.function().apply(this, this.arguments);
    }

    @Override
    public BoundingBox terrainBoundingBox() {
        if (segment != null) {
            BoundingBox localBoundingBox;
            if (segment.getLocalBoundingBox() != null)
                localBoundingBox = segment.getLocalBoundingBox();
            else localBoundingBox = segment.getDefaultLocalBoundingBox();

            return BBox.moved(localBoundingBox, levelPos);
        }

        return boundingBox;
    }

    public void setAdjustment(TerrainAdjustment adjustment) {
        this.adjustment = adjustment;
    }

    @Override
    public TerrainAdjustment terrainAdjustment() {
        return adjustment;
    }

    public BlockPos getLevelPos() {
        return levelPos;
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext contxt, CompoundTag tag) {
        tag.put(ROTATION_LABEL, packDirectly(rotation, BCSerializers.ROTATION.get()));
        tag.put(TERRAFORMER_LABEL, packDirectly(adjustment, BCSerializers.TERRAIN_ADJUSTMENT.get()));
        tag.put(LEVEL_POS_LABEL, packDirectly(levelPos, BCSerializers.BLOCKPOS.get()));
        tag.put(CHART_LABEL, packDirectly(chart, BCSerializers.CHART.get()));
        tag.putInt(ACTOR_LABEL, actor);
        tag.put(ARGUMENTS_LABEL, chart.serialize(actor, arguments));
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager manager,
                            ChunkGenerator chunkGenerator, RandomSource random,
                            BoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        if (!ran && chart != null) {
            ran = true;
            try {
                segment.start(level, posProcessor.process(level, levelPos), rotation);
            } catch (Exception e) {
                Braincell.common().logger.error("Failed to process segment at {}", levelPos);
                e.printStackTrace();
            }
        }
    }

    public ChartPiece make(TerrainAdjustment adjustment, BoundingBox localBoundingBox, BlockPos levelPos, Rotation rotation, int actor, Object... arguments) {
        ChartPiece piece = new ChartPiece(0, BBox.moved(localBoundingBox, levelPos), rotation, levelPos, this.chart, actor, arguments);
        piece.setAdjustment(adjustment);
        return piece;
    }
}