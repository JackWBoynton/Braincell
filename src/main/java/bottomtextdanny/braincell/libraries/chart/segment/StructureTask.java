package bottomtextdanny.braincell.libraries.chart.segment;

import bottomtextdanny.braincell.libraries.chart.help.BBox;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public interface StructureTask {

    static StructureTask blankPiece(BlockPos structurePos, BoundingBox box,
                                    TerrainAdjustment adjustment) {
        return (root, config, builder) -> {
            builder.addPiece(new VoidPiece(BBox.moved(box, root.offset(structurePos)), adjustment));
        };
    }

    static StructureTask segmentPiece(TerrainAdjustment adjustment, SegmentTicket ticket) {
        return (levelPos, config, builder) -> {
            levelPos = levelPos.offset(ticket.position());
            Segment<?> segment = ticket.segment();

            BoundingBox box = segment.getLocalBoundingBox();
            if (box == null) {
                box = segment.getDefaultLocalBoundingBox();
            }

            ticket.segment().setTicket(ticket);
            ticket.segment().structurePos = ticket.position();
            SegmentPiece piece = new SegmentPiece(0, BBox.moved(box, levelPos), levelPos, ticket.rotation(), segment);
            piece.setAdjustment(adjustment);
            builder.addPiece(piece);
        };
    }

    static StructureTask piece(StructurePiece piece) {
        return (levelPos, config, builder) -> {
            builder.addPiece(piece);
        };
    }

    static StructureTask segmentPiece(SegmentTicket ticket) {
        return (levelPos, config, builder) -> {
            levelPos = levelPos.offset(ticket.position());
            Segment<?> segment = ticket.segment();

            BoundingBox box = segment.getLocalBoundingBox();
            if (box == null) {
                box = segment.getDefaultLocalBoundingBox();
            }

            ticket.segment().setTicket(ticket);
            ticket.segment().structurePos = ticket.position();
            builder.addPiece(new SegmentPiece(0, BBox.moved(segment.getDefaultLocalBoundingBox(), levelPos), levelPos, ticket.rotation(), segment));
        };
    }

    void process(BlockPos root, Structure.GenerationContext config, StructurePiecesBuilder builder);
}
