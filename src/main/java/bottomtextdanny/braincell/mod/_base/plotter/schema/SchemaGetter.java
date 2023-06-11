package bottomtextdanny.braincell.mod._base.plotter.schema;

import java.util.random.RandomGenerator;
import net.minecraft.world.level.LevelAccessor;

@FunctionalInterface
public interface SchemaGetter {
   SchemaGetter DUMMY = (lvl, r) -> {
      return Schema.DUMMY;
   };

   Schema make(LevelAccessor var1, RandomGenerator var2);

   static SchemaGetter of(Schema schema) {
      return (lvl, r) -> {
         return schema;
      };
   }
}
