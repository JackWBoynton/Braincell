package bottomtextdanny.braincell.base.function;

import java.util.function.Function;

public final class BCFunctions {
   public static Function supply(Object value) {
      return (t) -> {
         return value;
      };
   }

   private BCFunctions() {
   }
}
