package bottomtextdanny.braincell.libraries.chart.steppy.step;

import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.libraries.chart.steppy.data.Data;
import bottomtextdanny.braincell.libraries.chart.steppy.iteration.TransientData;
import net.minecraft.tags.TagKey;

public interface TagKeyStep extends Step {

    TagKey<?> invokeTagKey(Data data, ObjectFetcher metadata, TransientData preceding);

    @Override
    default TagKey<?> invoke(Data data, ObjectFetcher metadata, TransientData preceding) {
        return invokeTagKey(data, metadata, preceding);
    }
}
