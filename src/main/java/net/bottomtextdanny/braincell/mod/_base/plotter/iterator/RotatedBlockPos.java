package net.bottomtextdanny.braincell.mod._base.plotter.iterator;

import net.bottomtextdanny.braincell.mod._base.plotter.PlotterRotator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.Vec3;

public class RotatedBlockPos extends BlockPos {
    public final Rotation rotation;

    public RotatedBlockPos(Rotation rotation, int x, int y, int z) {
        super(x, y, z);
        this.rotation = rotation;
    }

    public RotatedBlockPos(Rotation rotation, double x, double y, double z) {
        super(x, y, z);
        this.rotation = rotation;
    }

    public RotatedBlockPos(Rotation rotation, Vec3 vec) {
        this(rotation, vec.x, vec.y, vec.z);
    }

    public RotatedBlockPos(Rotation rotation, Position vec) {
        this(rotation, vec.x(), vec.y(), vec.z());
    }

    public RotatedBlockPos(Rotation rotation, Vec3i vec) {
        this(rotation, vec.getX(), vec.getY(), vec.getZ());
    }

    protected RotatedBlockPos getRotationOf(Vec3i vec) {
        return new RotatedBlockPos(this.rotation, PlotterRotator.rotatedOf(this.rotation, vec));
    }

    protected RotatedBlockPos getRotationOf(int x, int y, int z) {
        return new RotatedBlockPos(this.rotation, PlotterRotator.rotatedOf(this.rotation, x, y, z));
    }

    public RotatedBlockPos offset(double x, double y, double z) {
        return x == 0.0D && y == 0.0D && z == 0.0D ? this :
                new RotatedBlockPos(this.rotation, getRotationOf(this.getX() + (int)x,
                this.getY() + (int)y,
                        this.getZ() + (int)z));
    }

    public RotatedBlockPos offset(int x, int y, int z) {
        return x == 0 && y == 0 && z == 0 ? this :
                new RotatedBlockPos(this.rotation, getRotationOf(this.getX() + x, this.getY() + y, this.getZ() + z));
    }

    public RotatedBlockPos offset(Vec3i vec) {
        return this.offset(vec.getX(), vec.getY(), vec.getZ());
    }

    public RotatedBlockPos subtract(Vec3i vec) {
        return this.offset(-vec.getX(), -vec.getY(), -vec.getZ());
    }

    public RotatedBlockPos multiply(int scaler) {
        if (scaler == 1) {
            return this;
        } else {
            return new RotatedBlockPos(this.rotation, this.getX() * scaler, this.getY() * scaler, this.getZ() * scaler);
        }
    }

    public RotatedBlockPos above() {
        return this.relative(Direction.UP);
    }

    public RotatedBlockPos above(int p_121972_) {
        return this.relative(Direction.UP, p_121972_);
    }

    public RotatedBlockPos below() {
        return this.relative(Direction.DOWN);
    }

    public RotatedBlockPos below(int p_122000_) {
        return this.relative(Direction.DOWN, p_122000_);
    }

    public RotatedBlockPos north() {
        return this.relative(Direction.NORTH);
    }

    public RotatedBlockPos north(int p_122014_) {
        return this.relative(Direction.NORTH, p_122014_);
    }

    public RotatedBlockPos south() {
        return this.relative(Direction.SOUTH);
    }

    public RotatedBlockPos south(int p_122021_) {
        return this.relative(Direction.SOUTH, p_122021_);
    }

    public RotatedBlockPos west() {
        return this.relative(Direction.WEST);
    }

    public RotatedBlockPos west(int p_122026_) {
        return this.relative(Direction.WEST, p_122026_);
    }

    public RotatedBlockPos east() {
        return this.relative(Direction.EAST);
    }

    public RotatedBlockPos east(int p_122031_) {
        return this.relative(Direction.EAST, p_122031_);
    }

    public RotatedBlockPos relative(Direction direction) {
        direction = this.rotation.rotate(direction);
        return new RotatedBlockPos(this.rotation, this.getX() + direction.getStepX(), this.getY() + direction.getStepY(), this.getZ() + direction.getStepZ());
    }

    public RotatedBlockPos relative(Direction direction, int steps) {
        direction = this.rotation.rotate(direction);
        return steps == 0 ? this : new RotatedBlockPos(this.rotation, this.getX() + direction.getStepX() * steps, this.getY() + direction.getStepY() * steps, this.getZ() + direction.getStepZ() * steps);
    }

    public RotatedBlockPos relative(Direction.Axis axis, int steps) {
        axis = PlotterRotator.AXIS_ROTATOR.get(axis.ordinal()).apply(this.rotation);
        if (steps == 0) {
            return this;
        } else {
            int i = axis == Direction.Axis.X ? steps : 0;
            int j = axis == Direction.Axis.Y ? steps : 0;
            int k = axis == Direction.Axis.Z ? steps : 0;
            return new RotatedBlockPos(this.rotation, this.getX() + i, this.getY() + j, this.getZ() + k);
        }
    }

    public RotatedBlockPos rotate(Rotation rotation) {
        return new RotatedBlockPos(this.rotation, PlotterRotator.rotatedOf(this.rotation.getRotated(rotation), this));
    }

    public RotatedBlockPos cross(Vec3i other) {
        other = getRotationOf(other);
        return new RotatedBlockPos(this.rotation, this.getY() * other.getZ() - this.getZ() * other.getY(), this.getZ() * other.getX() - this.getX() * other.getZ(), this.getX() * other.getY() - this.getY() * other.getX());
    }

    public RotatedBlockPos atY(int yLevel) {
        return new RotatedBlockPos(this.rotation, this.getX(), yLevel, this.getZ());
    }

    public RotatedBlockPos immutable() {
        return this;
    }

    @Deprecated
    public MutableBlockPos mutable() {
        return new MutableBlockPos(this.getX(), this.getY(), this.getZ());
    }

    public Mutable mutableRotable() {
        return new Mutable(this.rotation, this.getX(), this.getY(), this.getZ());
    }

    public static class Mutable extends RotatedBlockPos {
        public Mutable(Rotation rotation) {
            this(rotation, 0, 0, 0);
        }

        public Mutable(Rotation rotation, int x, int y, int z) {
            super(rotation, x, y, z);
        }

        public Mutable(Rotation rotation, double x, double y, double z) {
            this(rotation, Mth.floor(x), Mth.floor(y), Mth.floor(z));
        }

        public RotatedBlockPos offset(double x, double y, double z) {
            return super.offset(x, y, z).immutable();
        }

        public RotatedBlockPos offset(int x, int y, int z) {
            return super.offset(x, y, z).immutable();
        }

        public RotatedBlockPos multiply(int scaler) {
            return super.multiply(scaler).immutable();
        }

        public RotatedBlockPos relative(Direction direction, int steps) {
            return super.relative(direction, steps).immutable();
        }

        public RotatedBlockPos relative(Direction.Axis axisDirection, int steps) {
            return super.relative(axisDirection, steps).immutable();
        }

        public RotatedBlockPos rotate(Rotation rotation) {
            return super.rotate(rotation).immutable();
        }

        public Mutable set(int x, int y, int z) {
            this.setX(x);
            this.setY(y);
            this.setZ(z);
            return this;
        }

        public Mutable set(double x, double y, double z) {
            return this.set(Mth.floor(x), Mth.floor(y), Mth.floor(z));
        }

        public Mutable set(Vec3i vec) {
            return this.set(vec.getX(), vec.getY(), vec.getZ());
        }

//        public Mutable set(AxisCycle axisCycle, int x, int y, int z) {
//            return this.set(axisCycle.cycle(x, y, z, Direction.Axis.X), axisCycle.cycle(x, y, z, Direction.Axis.Y), axisCycle.cycle(x, y, z, Direction.Axis.Z));
//        }

        public Mutable setWithOffset(Vec3i vec, Direction direction) {
            direction = this.rotation.rotate(direction);
            return this.set(vec.getX() + direction.getStepX(), vec.getY() + direction.getStepY(), vec.getZ() + direction.getStepZ());
        }

        public Mutable setWithOffset(Vec3i vec, int x, int y, int z) {
            Vec3i rotatedOffset = getRotationOf(x, y, z);
            return this.set(vec.getX() + rotatedOffset.getX(), vec.getY() + rotatedOffset.getY(), vec.getZ() + rotatedOffset.getZ());
        }

        public Mutable setWithOffset(Vec3i vec, Vec3i offset) {
            offset = getRotationOf(offset);
            return this.set(vec.getX() + offset.getX(), vec.getY() + offset.getY(), vec.getZ() + offset.getZ());
        }

        public Mutable move(Direction direction) {
            direction = this.rotation.rotate(direction);
            return this.move(direction, 1);
        }

        public Mutable move(Direction direction, int steps) {
            direction = this.rotation.rotate(direction);
            return this.set(this.getX() + direction.getStepX() * steps, this.getY() + direction.getStepY() * steps, this.getZ() + direction.getStepZ() * steps);
        }

        public Mutable move(int x, int y, int z) {
            Vec3i rotatedOffset = getRotationOf(x, y, z);
            return this.set(this.getX() + rotatedOffset.getX(), this.getY() + rotatedOffset.getY(), this.getZ() + rotatedOffset.getZ());
        }

        public Mutable move(Vec3i vec) {
            vec = getRotationOf(vec);
            return this.set(this.getX() + vec.getX(), this.getY() + vec.getY(), this.getZ() + vec.getZ());
        }

        public Mutable clamp(Direction.Axis p_122148_, int p_122149_, int p_122150_) {
            switch(p_122148_) {
                case X:
                    return this.set(Mth.clamp(this.getX(), p_122149_, p_122150_), this.getY(), this.getZ());
                case Y:
                    return this.set(this.getX(), Mth.clamp(this.getY(), p_122149_, p_122150_), this.getZ());
                case Z:
                    return this.set(this.getX(), this.getY(), Mth.clamp(this.getZ(), p_122149_, p_122150_));
                default:
                    throw new IllegalStateException("Unable to clamp axis " + p_122148_);
            }
        }

        public Mutable setX(int p_175341_) {
            super.setX(p_175341_);
            return this;
        }

        public Mutable setY(int p_175343_) {
            super.setY(p_175343_);
            return this;
        }

        public Mutable setZ(int p_175345_) {
            super.setZ(p_175345_);
            return this;
        }

        public RotatedBlockPos immutable() {
            return new RotatedBlockPos(this.rotation, this);
        }
    }
}
