package net.bottomtextdanny.braincell.base.function;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class Clearable<T> implements Supplier<T> {
    private final Supplier<T> provider;
    private @Nullable T maybeValue;
    private boolean gotten;

    private Clearable(Supplier<T> provider) {
        super();
        this.provider = provider;
    }

    public static <T> Clearable<T> of(Supplier<T> provider) {
        return new Clearable<>(provider);
    }

    public void clear() {
        if (this.gotten) {
            this.maybeValue = null;
            this.gotten = false;
        }
    }

    @Override
    public T get() {
        if (!this.gotten) {
            this.maybeValue = this.provider.get();
            this.gotten = true;
        }
        return this.maybeValue;
    }
}
