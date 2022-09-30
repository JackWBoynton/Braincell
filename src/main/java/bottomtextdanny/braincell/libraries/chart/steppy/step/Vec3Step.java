package bottomtextdanny.braincell.libraries.chart.steppy.step;

import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.libraries.chart.steppy.Vec3Steps;
import bottomtextdanny.braincell.libraries.chart.steppy.data.Data;
import bottomtextdanny.braincell.libraries.chart.steppy.iteration.TransientData;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public interface Vec3Step extends Step {

    Vec3 invokeVec3(Data data, ObjectFetcher metadata, TransientData preceding);

    default Vec3Steps.Offset offset(Step offset) {
        return new Vec3Steps.Offset(offset);
    }

    @Override
    default Vec3 invoke(Data data, ObjectFetcher metadata, TransientData preceding) {
        return invokeVec3(data, metadata, preceding);
    }
}
