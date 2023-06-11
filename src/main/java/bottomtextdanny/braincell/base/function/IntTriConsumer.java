package bottomtextdanny.braincell.base.function;

import java.util.Objects;

public interface IntTriConsumer {
   void accept(int var1, int var2, int var3);

   default IntTriConsumer andThen(IntTriConsumer after) {
      Objects.requireNonNull(after);
      return (t, u, v) -> {
         this.accept(t, u, v);
         after.accept(t, u, v);
      };
   }
}
