package bottomtextdanny.braincell.libraries.chart.help;

import bottomtextdanny.braincell.libraries.registry.Serializable;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import net.minecraft.world.phys.Vec3;

public interface Distance3 extends Serializable {

    double distance(double x1, double y1, double z1, double x2, double y2, double z2);

    default double distance(Vec3 vec1, Vec3 vec2) {
        return distance(vec1.x, vec1.y, vec1.z, vec2.x, vec2.y, vec2.z);
    }

    Wrap<? extends Serializer<? extends Distance3>> serializer();
}
