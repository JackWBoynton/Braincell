package bottomtextdanny.braincell.libraries.chart.help;

import bottomtextdanny.braincell.libraries.registry.Serializable;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import net.minecraft.world.phys.Vec3;

public interface GridWarper extends Serializable {

    Vec3 warp(Vec3 sampler);

    default GridWarper add(GridWarper warper) {
        GridWarpers.Concat list = new GridWarpers.Concat();
        list.add(this);
        list.add(warper);
        return list;
    }

    Wrap<? extends Serializer<? extends GridWarper>> serializer();
}
