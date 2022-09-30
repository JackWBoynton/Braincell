package bottomtextdanny.braincell.libraries.chart.help;

import bottomtextdanny.braincell.base.function.IntTriConsumer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class IntBox implements Cloneable {
    private static final List<BiConsumer<IntBox, Integer>> EXPAND_BY_DIRECTION = List.of(
            (b, a) -> b.y1 -= a,
            (b, a) -> b.y2 += a,
            (b, a) -> b.z1 -= a,
            (b, a) -> b.z2 += a,
            (b, a) -> b.x1 -= a,
            (b, a) -> b.x2 += a
    );
    private static final List<BiConsumer<IntBox, Integer>> EXPAND_BY_AXIS = List.of(
            (b, a) -> {
                b.x1 -= a;
                b.x2 += a;
            },
            (b, a) -> {
                b.y1 -= a;
                b.y2 += a;
            },
            (b, a) -> {
                b.z1 -= a;
                b.z2 += a;
            }
    );
    private static final List<Function<IntBox, IntPlane>> PLANE_BY_AXIS = List.of(
            (b) -> new IntPlane(b.z1, b.y1, b.z2, b.y2, Direction.Axis.X),
            (b) -> new IntPlane(b.x1, b.z1, b.x2, b.z2, Direction.Axis.Y),
            (b) -> new IntPlane(b.x1, b.y1, b.x2, b.y2, Direction.Axis.Z)
    );
    public int x1;
    public int y1;
    public int z1;
    public int x2;
    public int y2;
    public int z2;

    public IntBox(int x1, int y1, int z1, int x2, int y2, int z2) {
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
    }

    public IntBox expand(int amount) {
        this.x1 -= amount;
        this.y1 -= amount;
        this.z1 -= amount;
        this.x2 += amount;
        this.y2 += amount;
        this.z2 += amount;
        return this;
    }

    public IntBox expandTowards(int amount, Direction direction) {
        EXPAND_BY_DIRECTION.get(direction.ordinal()).accept(this, amount);
        return this;
    }

    public IntBox expandByAxis(int amount, Direction.Axis axis) {
        EXPAND_BY_AXIS.get(axis.ordinal()).accept(this, amount);
        return this;
    }

    public void iterate(IntTriConsumer actor) {
        for (int lx = this.x1; lx <= this.x2; lx++) {
            for (int ly = this.y1; ly <= this.y2; ly++) {
                for (int lz = this.z1; lz <= this.z2; lz++) {
                    actor.accept(lx, ly, lz);
                }
            }
        }
    }

    public IntPlane getPlane(Direction.Axis axis) {
        return PLANE_BY_AXIS.get(axis.ordinal()).apply(this);
    }

    public BoundingBox asBoundingBox() {
        return new BoundingBox(x1, y1, z1, x2, y2, z2);
    }

    @Override
    public IntBox clone() {
        return new IntBox(this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
    }
}
