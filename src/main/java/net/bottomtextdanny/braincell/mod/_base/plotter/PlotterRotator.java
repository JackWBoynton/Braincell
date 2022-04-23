package net.bottomtextdanny.braincell.mod._base.plotter;

import com.google.common.collect.Maps;
import net.bottomtextdanny.braincell.base.function.IntTriFunction;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;

public final class PlotterRotator {
    private static final List<IntTriFunction<BlockPos>> ROTATED_OF = List.of(
            (x, y, z) -> new BlockPos(x, y, z),
            (x, y, z) -> new BlockPos(-z, y, x),
            (x, y, z) -> new BlockPos(-x, y, -z),
            (x, y, z) -> new BlockPos(z, y, -x)
    );
    private static final List<Consumer<BlockPos.MutableBlockPos>> ROTATE = List.of(
            bp -> {},
            bp -> bp.set(-bp.getZ(), bp.getY(), bp.getX()),
            bp -> bp.set(-bp.getX(), bp.getY(), -bp.getZ()),
            bp -> bp.set(bp.getZ(), bp.getY(), -bp.getX())
    );
    public static final List<Function<Rotation, Direction.Axis>> AXIS_ROTATOR = List.of(
            rot -> rot == Rotation.CLOCKWISE_90 || rot == Rotation.COUNTERCLOCKWISE_90 ? Direction.Axis.Z : Direction.Axis.X,
            rot -> Direction.Axis.Y,
            rot -> rot == Rotation.CLOCKWISE_90 || rot == Rotation.COUNTERCLOCKWISE_90 ? Direction.Axis.X : Direction.Axis.Z
    );
    private static final Map<Property<?>, BiFunction<Rotation, BlockState, BlockState>> PROPERTY_ROTATOR = Util.make(() -> {
        Map<Property<?>, BiFunction<Rotation, BlockState, BlockState>> map = Maps.newHashMap();

        map.put(AXIS, (rot, bs) -> solveAxisProperty(AXIS, rot, bs));
        map.put(HORIZONTAL_AXIS, (rot, bs) -> solveAxisProperty(HORIZONTAL_AXIS, rot, bs));
        map.put(FACING, (rot, bs) -> solveDirectionProperty(FACING, rot, bs));
        map.put(FACING_HOPPER, (rot, bs) -> solveDirectionProperty(FACING_HOPPER, rot, bs));
        map.put(HORIZONTAL_FACING, (rot, bs) -> solveDirectionProperty(HORIZONTAL_FACING, rot, bs));
        map.put(ROTATION_16, (rot, bs) -> {
            int val = bs.getValue(ROTATION_16);
            return bs.setValue(ROTATION_16, rot.rotate(val, 16));
        });
        return map;
    });
    private static final List<BooleanProperty> SIMPLE_DIR_PROP = List.of(
            NORTH,
            SOUTH,
            EAST,
            WEST
    );
    private static final List<Property<RedstoneSide>> REDSTONE_DIR_PROP = List.of(
            NORTH_REDSTONE,
            SOUTH_REDSTONE,
            EAST_REDSTONE,
            WEST_REDSTONE
    );
    private static final List<Property<WallSide>> WALL_DIR_PROP = List.of(
            NORTH_WALL,
            SOUTH_WALL,
            EAST_WALL,
            WEST_WALL
    );

    public static BlockPos rotatedOf(Rotation rotation, Vec3i pos) {
        return ROTATED_OF.get(rotation.ordinal()).apply(pos.getX(), pos.getY(), pos.getZ());
    }

    public static BlockPos rotatedOf(Rotation rotation, int x, int y, int z) {
        return ROTATED_OF.get(rotation.ordinal()).apply(x, y, z);
    }

    public static void rotate(Rotation rotation, BlockPos.MutableBlockPos pos) {
        ROTATE.get(rotation.ordinal()).accept(pos);
    }

    public static BlockState rotateBlockState(Rotation rotation, BlockState blockState) {
        for (Property<?> property : blockState.getProperties()) {
            BiFunction<Rotation, BlockState, BlockState> rotatorFunc = PROPERTY_ROTATOR.get(property);

            if (rotatorFunc != null) {
                blockState = rotatorFunc.apply(rotation, blockState);
            }
        }

        if (blockState.hasProperty(NORTH)) {
            blockState = solveSingleDirectionProperties(SIMPLE_DIR_PROP, rotation, blockState);
        } else if (blockState.hasProperty(NORTH_WALL)) {
            blockState = solveSingleDirectionProperties(WALL_DIR_PROP, rotation, blockState);
        } else if (blockState.hasProperty(NORTH_REDSTONE)) {
            blockState = solveSingleDirectionProperties(REDSTONE_DIR_PROP, rotation, blockState);
        }

        return blockState;
    }

    private static BlockState solveAxisProperty(EnumProperty<Direction.Axis> axisProp, Rotation rot, BlockState bs) {
        return bs.setValue(axisProp, AXIS_ROTATOR.get(bs.getValue(axisProp).ordinal()).apply(rot));
    }
    private static BlockState solveDirectionProperty(DirectionProperty dirProp, Rotation rot, BlockState bs) {
        return bs.setValue(dirProp, rot.rotate(bs.getValue(dirProp)));
    }

    private static <V extends Comparable<V>> BlockState solveSingleDirectionProperties(List<? extends Property<V>> propertyList, Rotation rot, BlockState bs) {
        V north = bs.getValue(propertyList.get(0));
        V south = bs.getValue(propertyList.get(1));
        V east = bs.getValue(propertyList.get(2));
        V west = bs.getValue(propertyList.get(3));
        V rNorth = north;
        V rSouth = south;
        V rEast = east;
        V rWest = west;
        switch (rot) {
            case CLOCKWISE_90 -> {
                rNorth = west;
                rEast = north;
                rSouth = east;
                rWest = south;
            }
            case CLOCKWISE_180 -> {
                rNorth = south;
                rEast = west;
                rSouth = north;
                rWest = east;
            }
            case COUNTERCLOCKWISE_90 -> {
                rNorth = east;
                rEast = south;
                rSouth = west;
                rWest = north;
            }
            default -> {}
        }
        return bs.setValue(propertyList.get(0), rNorth)
                .setValue(propertyList.get(1), rEast)
                .setValue(propertyList.get(2), rSouth)
                .setValue(propertyList.get(3), rWest);
    }
}
