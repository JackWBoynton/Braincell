package bottomtextdanny.braincell.libraries.chart.segment;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.LinkedList;
import java.util.List;

import static bottomtextdanny.braincell.libraries.registry.Serializer.unpack;
import static bottomtextdanny.braincell.libraries.registry.Serializer.unpackDirectly;

public abstract class OldChartPiece extends SegmentPiece {
    protected final Structure.GenerationContext config;

    protected OldChartPiece(Structure.GenerationContext config, int genDepth) {
        super(genDepth, null, null, Rotation.NONE, null);
        this.config = config;
    }

    public void setNewStartingBox() {
        boundingBox = makeStartingBox(config);
        levelPos = new BlockPos(
                boundingBox.minX() + boundingBox.getXSpan() / 2,
                boundingBox.minY(),
                boundingBox.minZ() + boundingBox.getZSpan() / 2);
    }

    public abstract BoundingBox makeStartingBox(Structure.GenerationContext config);

    public void setNewRoot(StructurePiecesBuilder builder) {
        List<StructureTask> list = new LinkedList<>();

        builder.addPiece(this);

        segment = makeRoot(config, list);

        for (StructureTask task : list) {
            task.process(levelPos, config, builder);
        }
    }

    public abstract Segment<?> makeRoot(Structure.GenerationContext config, List<StructureTask> pieceBoxes);

    public static BlockPos realPosOffsetY(BlockPos base, int realY) {
        return new BlockPos(base.getX(), base.getY() - realY, base.getZ());
    }

    public int getHeightAt(int x, int z, Heightmap.Types type) {
        return config.chunkGenerator().getFirstFreeHeight(x, z, type, config.heightAccessor(), config.randomState());
    }
}
