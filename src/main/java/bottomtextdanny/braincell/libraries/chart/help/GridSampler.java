package bottomtextdanny.braincell.libraries.chart.help;

import bottomtextdanny.braincell.libraries.registry.Serializable;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public interface GridSampler extends Serializable {

    float value(float x, float y, float z);

    default float value(BlockPos pos) {
        return value(pos.getX(), pos.getY(), pos.getZ());
    }

    default float value(Vec3 pos) {
        return value((float) pos.x(), (float) pos.y(), (float) pos.z());
    }

    default GridSamplers.OperationConcat sum(GridSampler sampler) {
        GridSamplers.OperationConcat concat = new GridSamplers.OperationConcat();
        concat.sum(this);
        concat.sum(sampler);
        return concat;
    }

    default GridSamplers.OperationConcat sum(float addition) {
        return sum(new GridSamplers.Fixed(addition));
    }

    default GridSamplers.OperationConcat scale(GridSampler sampler) {
        GridSamplers.OperationConcat concat = new GridSamplers.OperationConcat();
        concat.sum(this);
        concat.scale(sampler);
        return concat;
    }

    default GridSamplers.OperationConcat scale(float scalar) {
        return scale(new GridSamplers.Fixed(scalar));
    }

    default GridSamplers.OperationConcat divide(GridSampler sampler) {
        GridSamplers.OperationConcat concat = new GridSamplers.OperationConcat();
        concat.sum(this);
        concat.divide(sampler);
        return concat;
    }

    default GridSamplers.OperationConcat divide(float denominator) {
        return divide(new GridSamplers.Fixed(denominator));
    }

    default GridSamplers.OperationConcat mod(GridSampler sampler) {
        GridSamplers.OperationConcat concat = new GridSamplers.OperationConcat();
        concat.sum(this);
        concat.mod(sampler);
        return concat;
    }



    default GridSamplers.OperationConcat mod(float modulo) {
        return mod(new GridSamplers.Fixed(modulo));
    }

    Wrap<? extends Serializer<? extends GridSampler>> serializer();
}
