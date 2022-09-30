package bottomtextdanny.braincell.libraries.chart.help;

import bottomtextdanny.braincell.base.Axis2D;
import bottomtextdanny.braincell.base.function.IntBiConsumer;
import bottomtextdanny.braincell.base.function.IntTriConsumer;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;

import java.util.List;

public class IntPlane implements Cloneable {
    public static List<CoordFixer> FIXER_BY_AXIS = List.of(
            (x, y) -> new Vec3i(0, y, x),
            (x, y) -> new Vec3i(x, 0, y),
            (x, y) -> new Vec3i(x, y, 0)
    );
    public int x1;
    public int y1;
    public int x2;
    public int y2;
    private Direction.Axis axis;

    public IntPlane(int x1, int y1, int x2, int y2, Direction.Axis axis) {
        super();
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.axis = axis;
    }

    public Direction.Axis getAxis() {
        return axis;
    }

    public void setAxis(Direction.Axis axis) {
        this.axis = axis;
    }

    public IntPlane expand(Axis2D axis, int amount) {
        if (axis == Axis2D.X) {
            this.x1 -= amount;
            this.x2 += amount;
        } else {
            this.y1 -= amount;
            this.y2 += amount;
        }
        return this;
    }

    public void iterate(IntTriConsumer actor) {
        for (int x = this.x1; x <= this.x2; x++) {
            for (int y = this.y1; y <= this.y2; y++) {
                Vec3i vec = FIXER_BY_AXIS.get(axis.ordinal()).apply(x, y);
                actor.accept(vec.getX(), vec.getY(), vec.getZ());
            }
        }
    }

    public void iteratePlane(IntBiConsumer actor) {
        for (int x = this.x1; x <= this.x2; x++) {
            for (int y = this.y1; y <= this.y2; y++) {
                actor.accept(x, y);
            }
        }
    }

    @Override
    public IntPlane clone() {
        return new IntPlane(this.x1, this.y1, this.x2, this.y2, this.axis);
    }

    public static Vec3i fixCoords(Direction.Axis axis, int x, int y) {
        return FIXER_BY_AXIS.get(axis.ordinal()).apply(x, y);
    }

    @FunctionalInterface
    public interface CoordFixer {
        Vec3i apply(int x, int y);
    }
}
