package bottomtextdanny.braincell.mod._base.plotter.schema;

import net.minecraft.world.level.LevelAccessor;

import java.util.random.RandomGenerator;

@FunctionalInterface
public interface SchemaGetter {
    SchemaGetter DUMMY = (lvl, r) -> Schema.DUMMY;

    Schema make(LevelAccessor level, RandomGenerator random);

    static SchemaGetter of(Schema schema) {
        return (lvl, r) -> schema;
    }
}
