package bottomtextdanny.braincell.libraries.chart.help;

import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import bottomtextdanny.braincell.tables.BCSerializers;
import net.minecraft.world.phys.Vec3;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public final class GridWarpers {
    private static final GridWarper IDENTITY = new GridWarper() {

        @Override
        public Vec3 warp(Vec3 sampler) {
            return sampler;
        }

        @Override
        public Wrap<? extends Serializer<? extends GridWarper>> serializer() {
            return BCSerializers.GRID_WARPER_IDENTITY;
        }
    };

    public static GridWarper gwIdentity() {
        return IDENTITY;
    }

    public static Concat gwConcat() {
        return new Concat();
    }

    public static Mul gwMul(float x, float y, float z) {
        return new Mul(x, y, z);
    }

    public static Scale gwScale(float scalar) {
        return new Scale(scalar);
    }

    public static Offset gwOffset(float x, float y, float z) {
        return new Offset(x, y, z);
    }

    public static FixX gwFixX(float value) {
        return new FixX(value);
    }

    public static FixY gwFixY(float value) {
        return new FixY(value);
    }

    public static FixZ gwFixZ(float value) {
        return new FixZ(value);
    }

    public static class Concat implements GridWarper {
        private final List<GridWarper> list = new LinkedList<>();

        @Override
        public GridWarpers.Concat add(GridWarper warper) {

            list.add(warper);
            return this;
        }

        @Override
        public Vec3 warp(Vec3 sampler) {
            for (GridWarper gridWarper : list) {
                sampler = gridWarper.warp(sampler);
            }
            return sampler;
        }

        public void iterate(Consumer<GridWarper> consumer) {
            list.forEach(consumer);
        }

        @Override
        public Wrap<? extends Serializer<? extends GridWarper>> serializer() {
            return BCSerializers.GRID_WARPER_CONCAT;
        }
    }

    public record Mul(float x, float y, float z) implements GridWarper {

        @Override
        public Vec3 warp(Vec3 sampler) {
            return sampler.multiply(x, y, z);
        }

        @Override
        public Wrap<? extends Serializer<? extends GridWarper>> serializer() {
            return BCSerializers.GRID_WARPER_MULTIPLICATION;
        }
    }

    public record Scale(float scalar) implements GridWarper {

        @Override
        public Vec3 warp(Vec3 sampler) {
            return sampler.scale(scalar);
        }

        @Override
        public Wrap<? extends Serializer<? extends GridWarper>> serializer() {
            return BCSerializers.GRID_WARPER_SCALE;
        }
    }

    public record Offset(float x, float y, float z) implements GridWarper {

        @Override
        public Vec3 warp(Vec3 sampler) {
            return sampler.add(x, y, z);
        }

        @Override
        public Wrap<? extends Serializer<? extends GridWarper>> serializer() {
            return BCSerializers.GRID_WARPER_OFFSET;
        }
    }

    public record FixX(float value) implements GridWarper {

        @Override
        public Vec3 warp(Vec3 sampler) {
            return new Vec3(value, sampler.y, sampler.z);
        }

        @Override
        public Wrap<? extends Serializer<? extends GridWarper>> serializer() {
            return BCSerializers.GRID_WARPER_FIX_X;
        }
    }

    public record FixY(float value) implements GridWarper {

        @Override
        public Vec3 warp(Vec3 sampler) {
            return new Vec3(sampler.x, value, sampler.z);
        }

        @Override
        public Wrap<? extends Serializer<? extends GridWarper>> serializer() {
            return BCSerializers.GRID_WARPER_FIX_Y;
        }
    }

    public record FixZ(float value) implements GridWarper {

        @Override
        public Vec3 warp(Vec3 sampler) {
            return new Vec3(sampler.x, sampler.y, value);
        }

        @Override
        public Wrap<? extends Serializer<? extends GridWarper>> serializer() {
            return BCSerializers.GRID_WARPER_FIX_Z;
        }
    }
}
