package bottomtextdanny.braincell.base.function;

import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract sealed class Lazy<T> implements Supplier<T> {
    protected T value;

    private Lazy() {
        super();
    }

    public static <T> Lazy<T> of(Supplier<T> provider) {
        return new Lazy.Normal<>(provider);
    }

    public static <T> Lazy<T> failNull(Supplier<T> provider) {
        return new Lazy.FailNull<>(provider);
    }

    public static <T> Lazy<T> failable(Function<MutableBoolean, T> provider) {
        return new Lazy.Failable<>(provider);
    }

    public T get() {
        return isBuilt() ? value : build();
    }

    protected abstract T build();

    public abstract boolean isBuilt();

    private static final class Normal<T> extends Lazy<T> {
        private Supplier<T> provider;

        public Normal(Supplier<T> provider) {
            this.provider = provider;
        }

        @Override
        protected T build() {
            value = provider.get();
            provider = null;
            return value;
        }

        @Override
        public boolean isBuilt() {
            return provider == null;
        }
    }

    private static final class FailNull<T> extends Lazy<T> {
        private Supplier<T> provider;

        public FailNull(Supplier<T> provider) {
            this.provider = provider;
        }

        @Override
        protected T build() {
            value = provider.get();
            if (value == null) provider = null;
            return value;
        }

        @Override
        public boolean isBuilt() {
            return provider == null;
        }
    }

    private static final class Failable<T> extends Lazy<T> {
        private Function<MutableBoolean, T> provider;
        private MutableBoolean valid = new MutableBoolean();

        public Failable(Function<MutableBoolean, T> provider) {
            this.provider = provider;
        }

        @Override
        protected T build() {
            valid.setTrue();
            value = provider.apply(valid);
            if (valid.isFalse()) {
                valid = null;
                provider = null;
            }
            return value;
        }

        @Override
        public boolean isBuilt() {
            return provider == null;
        }
    }
}
