package bottomtextdanny.braincell.base.function;

import java.util.Objects;

public interface IntTriConsumer {
    void accept(int t, int u, int v);

    default IntTriConsumer andThen(IntTriConsumer after) {
        Objects.requireNonNull(after);
        return (int t, int u, int v) -> {
            accept(t, u, v); after.accept(t, u, v);
        };
    }
}
