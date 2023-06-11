package bottomtextdanny.braincell.base.function;

import java.util.Objects;

public interface IntBiConsumer {
   void accept(int var1, int var2);

   default IntBiConsumer andThen(IntBiConsumer after) {
      Objects.requireNonNull(after);
      return (t, u) -> {
         this.accept(t, u);
         after.accept(t, u);
      };
   }
}
