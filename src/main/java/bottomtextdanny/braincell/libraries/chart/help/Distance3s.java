package bottomtextdanny.braincell.libraries.chart.help;

import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import bottomtextdanny.braincell.tables.BCSerializers;

public final class Distance3s {
    private static final Euclidean EUCLIDEAN = new Euclidean();
    private static final Squared SQUARED = new Squared();
    private static final Manhattan MANHATTAN = new Manhattan();
    private static final Chebyshev CHEBYSHEV = new Chebyshev();

    public static Euclidean d3Euclid() {
        return EUCLIDEAN;
    }

    public static Squared d3Squared() {
        return SQUARED;
    }

    public static Manhattan d3Manhat() {
        return MANHATTAN;
    }

    public static Chebyshev d3Cheby() {
        return CHEBYSHEV;
    }

    public record Euclidean() implements Distance3 {

        @Override
        public double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
            x1 = x1 - x2;
            y1 = y1 - y2;
            z1 = z1 - z2;
            return Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1);
        }

        @Override
        public Wrap<? extends Serializer<? extends Distance3>> serializer() {
            return BCSerializers.DISTANCE3_EUCLIDEAN;
        }
    }

    public record Squared() implements Distance3 {

        @Override
        public double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
            x1 = x1 - x2;
            y1 = y1 - y2;
            z1 = z1 - z2;
            return x1 * x1 + y1 * y1 + z1 * z1;
        }

        @Override
        public Wrap<? extends Serializer<? extends Distance3>> serializer() {
            return BCSerializers.DISTANCE3_SQUARED;
        }
    }

    public record Manhattan() implements Distance3 {

        @Override
        public double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
            return Math.abs(x1 - x2) + Math.abs(y1 - y2) + Math.abs(z1 - z2);
        }

        @Override
        public Wrap<? extends Serializer<? extends Distance3>> serializer() {
            return BCSerializers.DISTANCE3_MANHATTAN;
        }
    }

    public record Chebyshev() implements Distance3 {

        @Override
        public double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
            return Math.max(Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)), Math.abs(z1 - z2));
        }

        @Override
        public Wrap<? extends Serializer<? extends Distance3>> serializer() {
            return BCSerializers.DISTANCE3_CHEBYSHEV;
        }
    }
}
