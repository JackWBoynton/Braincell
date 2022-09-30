package bottomtextdanny.braincell.libraries.chart.help;

import bottomtextdanny.braincell.libraries.registry.Serializable;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import net.minecraft.world.phys.Vec2;

public interface Distance2 extends Serializable {

    double distance(double x1, double y1, double x2, double y2);

    default double distance(Vec2 vec1, Vec2 vec2) {
        return distance(vec1.x, vec1.y, vec2.x, vec2.y);
    }

    Wrap<? extends Serializer<? extends Distance2>> serializer();
}
