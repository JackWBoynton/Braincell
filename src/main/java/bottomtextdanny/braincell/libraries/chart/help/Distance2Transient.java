package bottomtextdanny.braincell.libraries.chart.help;

import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import bottomtextdanny.braincell.tables.BCSerializers;

@FunctionalInterface
public interface Distance2Transient extends Distance2 {

    @Override
    default Wrap<? extends Serializer<? extends Distance2>> serializer() {
        return BCSerializers.DISTANCE2_MANHATTAN;
    }
}
