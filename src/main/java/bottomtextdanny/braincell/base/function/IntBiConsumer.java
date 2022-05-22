package bottomtextdanny.braincell.base.function;

import java.util.Objects;

public interface IntBiConsumer {
    void accept(int t, int u);

    default IntBiConsumer andThen(IntBiConsumer after) {
        Objects.requireNonNull(after);
        return (int t, int u) -> {
            accept(t, u); after.accept(t, u);
        };
    }
}
