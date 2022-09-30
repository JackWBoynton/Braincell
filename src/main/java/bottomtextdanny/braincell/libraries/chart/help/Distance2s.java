package bottomtextdanny.braincell.libraries.chart.help;

import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import bottomtextdanny.braincell.tables.BCSerializers;

public final class Distance2s {
    private static final Euclidean EUCLIDEAN = new Euclidean();
    private static final Squared SQUARED = new Squared();
    private static final Manhattan MANHATTAN = new Manhattan();
    private static final Chebyshev CHEBYSHEV = new Chebyshev();

    public static Euclidean d2Euclid() {
        return EUCLIDEAN;
    }

    public static Squared d2Squared() {
        return SQUARED;
    }

    public static Manhattan d2Manhat() {
        return MANHATTAN;
    }

    public static Chebyshev d2Cheby() {
        return CHEBYSHEV;
    }

    public record Euclidean() implements Distance2 {

        @Override
        public double distance(double x1, double y1, double x2, double y2) {
            x1 = x1 - x2;
            y1 = y1 - y2;
            return Math.sqrt(x1 * x1 + y1 * y1);
        }

        @Override
        public Wrap<? extends Serializer<? extends Distance2>> serializer() {
            return BCSerializers.DISTANCE2_EUCLIDEAN;
        }
    }

    public record Squared() implements Distance2 {

        @Override
        public double distance(double x1, double y1, double x2, double y2) {
            x1 = x1 - x2;
            y1 = y1 - y2;
            return x1 * x1 + y1 * y1;
        }

        @Override
        public Wrap<? extends Serializer<? extends Distance2>> serializer() {
            return BCSerializers.DISTANCE2_SQUARED;
        }
    }

    public record Manhattan() implements Distance2 {

        @Override
        public double distance(double x1, double y1, double x2, double y2) {
            return Math.abs(x1 - x2) + Math.abs(y1 - y2);
        }

        @Override
        public Wrap<? extends Serializer<? extends Distance2>> serializer() {
            return BCSerializers.DISTANCE2_MANHATTAN;
        }
    }

    public record Chebyshev() implements Distance2 {

        @Override
        public double distance(double x1, double y1, double x2, double y2) {
            return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
        }

        @Override
        public Wrap<? extends Serializer<? extends Distance2>> serializer() {
            return BCSerializers.DISTANCE2_CHEBYSHEV;
        }
    }
}
