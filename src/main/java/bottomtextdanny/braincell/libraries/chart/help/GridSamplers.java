package bottomtextdanny.braincell.libraries.chart.help;

import bottomtextdanny.braincell.base.pair.Pair;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import bottomtextdanny.braincell.tables.BCSerializers;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

import java.util.LinkedList;
import java.util.function.Consumer;

public final class GridSamplers {
    private static final GridSampler NO_GRID_SAMPLER = new GridSampler() {

        @Override
        public float value(float x, float y, float z) {
            return 0;
        }

        @Override
        public Wrap<? extends Serializer<? extends GridSampler>> serializer() {
            return BCSerializers.GRID_SAMPLER_BLANK;
        }
    };
    private static final SimplexNoise[] SIMPLEX;
    private static final ImprovedNoise[] IMPROVED_NOISE;

    private static final int LAYERS = 16;

    static {
        SIMPLEX = new SimplexNoise[LAYERS];
        IMPROVED_NOISE = new ImprovedNoise[LAYERS];

        for (int i = 0; i < LAYERS; i++) {
            RandomSource source = RandomSource.create(i);
            SIMPLEX[i] = new SimplexNoise(source);
            IMPROVED_NOISE[i] = new ImprovedNoise(source);
        }
    }

    public static GridSampler gsBlank() {
        return NO_GRID_SAMPLER;
    }

    public static SimplexLayer gsSimplex(int layer) {
        return new SimplexLayer(layer);
    }

    public static SimplexLayer gsSimplex() {
        return new SimplexLayer(0);
    }

    public static ImprovedNoiseLayer gsImprovedNoise(int layer) {
        return new ImprovedNoiseLayer(layer);
    }

    public static ImprovedNoiseLayer gsImprovedNoise() {
        return new ImprovedNoiseLayer(0);
    }

    public static Fixed gsFixed(float value) {
        return new Fixed(value);
    }

    public static OperationConcat gsOp() {
        return new OperationConcat();
    }

    public static final class SimplexLayer implements GridSampler {
        private final int layer;

        public SimplexLayer(int layer) {
            this.layer = Mth.clamp(layer, 0, LAYERS);
        }

        @Override
        public float value(float x, float y, float z) {
            return (float) (SIMPLEX[layer]).getValue(x, y, z);
        }

        @Override
        public Wrap<? extends Serializer<? extends GridSampler>> serializer() {
            return BCSerializers.GRID_SAMPLER_SIMPLEX;
        }

        public int layer() {
            return layer;
        }
    }

    public static final class ImprovedNoiseLayer implements GridSampler {
        private final int layer;

        public ImprovedNoiseLayer(int layer) {
            this.layer = Mth.clamp(layer, 0, LAYERS);
        }

        @Override
        public float value(float x, float y, float z) {
            return (float) (IMPROVED_NOISE[layer]).noise(x, y, z);
        }

        @Override
        public Wrap<? extends Serializer<? extends GridSampler>> serializer() {
            return BCSerializers.GRID_SAMPLER_IMPROVED_NOISE;
        }

        public int layer() {
            return layer;
        }
    }

    public record Fixed(float value) implements GridSampler {

        @Override
        public float value(float x, float y, float z) {
            return value;
        }

        @Override
        public Wrap<? extends Serializer<? extends GridSampler>> serializer() {
            return BCSerializers.GRID_SAMPLER_FIXED;
        }
    }

    public static class OperationConcat implements GridSampler {
        public static final byte SUM = 0;
        public static final byte SCALE = 1;
        public static final byte DIVIDE = 2;
        public static final byte MOD = 3;
        private final LinkedList<Pair<Byte, GridSampler>> list = new LinkedList<>();

        @Override
        public float value(float x, float y, float z) {
            float value = 0;

            for (Pair<Byte, GridSampler> gridSampler : list) {
                switch (gridSampler.left()) {
                    case SUM -> value += gridSampler.right().value(x, y, z);
                    case SCALE -> value *= gridSampler.right().value(x, y, z);
                    case DIVIDE -> value /= gridSampler.right().value(x, y, z);
                    case MOD -> value %= gridSampler.right().value(x, y, z);
                }
            }
            return value;
        }

        @Override
        public OperationConcat sum(GridSampler sampler) {
            list.add(Pair.of(SUM, sampler));
            return this;
        }

        @Override
        public OperationConcat scale(GridSampler sampler) {
            list.add(Pair.of(SCALE, sampler));
            return this;
        }

        @Override
        public OperationConcat divide(GridSampler sampler) {
            list.add(Pair.of(DIVIDE, sampler));
            return this;
        }

        @Override
        public OperationConcat mod(GridSampler sampler) {
            list.add(Pair.of(MOD, sampler));
            return this;
        }

        public GridSamplers.OperationConcat op(byte op, GridSampler sampler) {
            list.add(Pair.of(op, sampler));
            return this;
        }

        public void iterate(Consumer<Pair<Byte, GridSampler>> consumer) {
            list.forEach(consumer);
        }

        @Override
        public Wrap<? extends Serializer<? extends GridSampler>> serializer() {
            return BCSerializers.GRID_SAMPLER_OPERATION_CONCAT;
        }
    }
}
