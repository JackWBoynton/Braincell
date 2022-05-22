package bottomtextdanny.braincell.base.vector;

import net.minecraft.world.phys.Vec2;

public interface DistanceCalc2 {
    DistanceCalc2 EUCLIDEAN = (x1, y1, x2, y2) -> {
        x1 = x1 - x2;
        y1 = y1 - y2;
        return Math.sqrt(x1 * x1 + y1 * y1);
    };
    DistanceCalc2 MANHATTAN = (x1, y1, x2, y2) -> {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    };
    DistanceCalc2 CHEBYSHEV = (x1, y1, x2, y2) -> {
        return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
    };

    double distance(double x1, double y1, double x2, double y2);

    default double distance(Vec2 vec1, Vec2 vec2) {
        return distance(vec1.x, vec1.y, vec2.x, vec2.y);
    }
}
