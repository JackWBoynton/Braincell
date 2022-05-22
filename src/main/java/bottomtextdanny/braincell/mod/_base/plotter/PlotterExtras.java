package bottomtextdanny.braincell.mod._base.plotter;

import bottomtextdanny.braincell.base.function.IntTriFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;

import java.util.List;
import java.util.function.ToIntFunction;

public final class PlotterExtras {
    public static final List<IntTriFunction<BlockPos>> AXIS_POS_GETTER = List.of(
            (axisPos, off1, off2) -> new BlockPos(axisPos, off2, off1),
            (axisPos, off1, off2) -> new BlockPos(off1, axisPos, off2),
            (axisPos, off1, off2) -> new BlockPos(off1, off2, axisPos)
    );

    public static final List<AxisPosSetter> AXIS_POS_SETTER = List.of(
            (pos, axisPos, off1, off2) -> pos.set(axisPos, off2, off1),
            (pos, axisPos, off1, off2) -> pos.set(off1, axisPos, off2),
            (pos, axisPos, off1, off2) -> pos.set(off1, off2, axisPos)
    );

    public static final List<ToIntFunction<Vec3i>> AXIS_COORD_GETTER = List.of(Vec3i::getX, Vec3i::getY, Vec3i::getZ);

    public static int getAxisCoord(Direction.Axis axis, Vec3i vec) {
        return AXIS_COORD_GETTER.get(axis.ordinal()).applyAsInt(vec);
    }

    public static BlockPos getAxisPos(Direction.Axis axis, int axisPos, int offPos1, int offPos2) {
        return AXIS_POS_GETTER.get(axis.ordinal()).apply(axisPos, offPos1, offPos2);
    }

    public static void setAxisPos(BlockPos.MutableBlockPos pos, Direction.Axis axis, int axisPos, int offPos1, int offPos2) {
        AXIS_POS_SETTER.get(axis.ordinal()).accept(pos, axisPos, offPos1, offPos2);
    }

    @FunctionalInterface
    public interface AxisPosSetter {
        void accept(BlockPos.MutableBlockPos bp, int axisPos, int offset1, int offset2);
    }
}
