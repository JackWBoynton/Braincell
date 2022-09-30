package bottomtextdanny.braincell.libraries.chart.steppy;

import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.libraries.chart.steppy.data.*;
import bottomtextdanny.braincell.libraries.chart.steppy.iteration.TransientData;
import bottomtextdanny.braincell.libraries.chart.steppy.step.Step;
import bottomtextdanny.braincell.libraries.chart.steppy.step.Vec3Step;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import bottomtextdanny.braincell.tables.BCStepDataTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

public final class Vec3Steps {
    private static final Vec3 ONE = new Vec3(1, 1, 1);

    @DataRequires(LevelPosArgument.class)
    @StepType(Type.ROOT)
    public record LevelArgument() implements Vec3Step {
        public static final Vec3Step INSTANCE = new LevelArgument();

        @Override
        public Vec3 invokeVec3(Data data, ObjectFetcher metadata, TransientData preceding) {
            BlockPos blockPos = data.cast(LevelPosArgument.class).levelPos();
            return new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.ROOT)
    public record Fixed(Vec3 vector) implements Vec3Step {

        @Override
        public Vec3 invokeVec3(Data data, ObjectFetcher metadata, TransientData preceding) {
            return vector;
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.CONTEXT_FUNCTION)
    public record Offset(Step offsetVector) implements Vec3Step {

        @Override
        public Vec3 invokeVec3(Data data, ObjectFetcher metadata, TransientData preceding) {
            return preceding.getOrDefault(BCStepDataTypes.VEC3.get()).add(offsetVector.invokeCast(data, metadata, preceding, Vec3.ZERO));
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @StepType(Type.CONTEXT_FUNCTION)
    public record Multiply(Step scalarVector) implements Vec3Step {

        @Override
        public Vec3 invokeVec3(Data data, ObjectFetcher metadata, TransientData preceding) {
            return preceding.getOrDefault(BCStepDataTypes.VEC3.get()).multiply(scalarVector.invokeCast(data, metadata, preceding, ONE));
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }

    @DataRequires(ReadableLevelArgument.class)
    @StepType(Type.CONTEXT_FUNCTION)
    public record ToHeight(Heightmap.Types type) implements Vec3Step {

        @Override
        public Vec3 invokeVec3(Data data, ObjectFetcher metadata, TransientData preceding) {
            Vec3 vec = preceding.getOrDefault(BCStepDataTypes.VEC3.get());
            return new Vec3(vec.x, data.cast(ReadableLevelArgument.class).readableLevel().getHeight(type, (int) vec.x, (int) vec.z), vec.z);
        }

        @Override
        public Wrap<? extends Serializer<?>> serializer() {
            return null;
        }
    }
}
