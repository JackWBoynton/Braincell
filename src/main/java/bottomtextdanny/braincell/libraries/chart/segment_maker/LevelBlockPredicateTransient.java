package bottomtextdanny.braincell.libraries.chart.segment_maker;

import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import bottomtextdanny.braincell.tables.BCSerializers;

@FunctionalInterface
public interface LevelBlockPredicateTransient extends LevelBlockPredicate {

    @Override
    default Wrap<? extends Serializer<? extends LevelBlockPredicate>> serializer() {
        return BCSerializers.LEVEL_BLOCK_PREDICATE_CANCEL;
    }
}
