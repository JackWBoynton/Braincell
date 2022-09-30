package bottomtextdanny.braincell.libraries.chart.help;

import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import bottomtextdanny.braincell.tables.BCSerializers;

@FunctionalInterface
public interface Distance3Transient extends Distance3 {

    @Override
    default Wrap<? extends Serializer<? extends Distance3>> serializer() {
        return BCSerializers.DISTANCE3_MANHATTAN;
    }
}
