package net.bottomtextdanny.braincell.base.vector;

public interface DistanceCalc {
    DistanceCalc EUCLIDEAN = (x1, y1, z1, x2, y2, z2) -> {
        x1 = x1 - x2; y1 = y1 - y2; z1 = z1 - z2;
        return Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1);
    };
    DistanceCalc MANHATTAN = (x1, y1, z1, x2, y2, z2) -> {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2) + Math.abs(z1 - z2);
    };
    DistanceCalc CHEBYSHEV = (x1, y1, z1, x2, y2, z2) -> {
        return Math.max(Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)), Math.abs(z1 - z2));
    };

    double distance(double x1, double y1, double z1, double x2, double y2, double z2);
}
