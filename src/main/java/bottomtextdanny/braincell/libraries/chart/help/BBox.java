package bottomtextdanny.braincell.libraries.chart.help;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public final class BBox {
    public static final BoundingBox ONE = new BoundingBox(BlockPos.ZERO);

    public static BoundingBox moved(BoundingBox box, BlockPos pos) {
        return box.moved(pos.getX(), pos.getY(), pos.getZ());
    }

    public static BoundingBox trans(BoundingBox box, BlockPos pos, int up, int side) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        return new BoundingBox(x + box.minX() - side, y + box.minY(), z + box.minZ() - side,
            x + box.minX() + side, y + box.minY() + up, z + box.minZ() + side);
    }

    public static BoundingBox trans(BoundingBox box, BlockPos pos, int up, int xSide, int zSide) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        return new BoundingBox(x + box.minX() - xSide, y + box.minY(), z + box.minZ() - zSide,
            x + box.minX() + xSide, y + box.minY() + up, z + box.minZ() + zSide);
    }
}
