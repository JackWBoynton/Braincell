/*
 * Copyright Sunday August 07 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.chart.segment;

import bottomtextdanny.braincell.libraries.chart.help.BBox;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.LinkedList;
import java.util.List;

public abstract class ChartRootPiece extends ChartPiece {
	protected final Structure.GenerationContext config;

	protected ChartRootPiece(Structure.GenerationContext config, int genDepth, Chart chart, int actor,
							 Object... arguments) {
		super(genDepth, BBox.ONE, Rotation.NONE, BlockPos.ZERO, chart, actor, arguments);
		this.config = config;
	}

	public ChartRootPiece(CompoundTag root) {
		super(root);
		config = null;
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

		makeSegment();
		handle(config, list);

		for (StructureTask task : list) {
			task.process(levelPos, config, builder);
		}
	}

	public abstract void handle(Structure.GenerationContext config, List<StructureTask> pieceBoxes);

	public static BlockPos realPosOffsetY(BlockPos base, int realY) {
		return new BlockPos(base.getX(), base.getY() - realY, base.getZ());
	}

	public int getHeightAt(int x, int z, Heightmap.Types type) {
		return config.chunkGenerator().getFirstFreeHeight(x, z, type, config.heightAccessor(), config.randomState());
	}
}
