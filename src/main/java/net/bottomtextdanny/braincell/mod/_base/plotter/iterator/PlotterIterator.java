package net.bottomtextdanny.braincell.mod._base.plotter.iterator;

import java.util.Objects;

@FunctionalInterface
public interface PlotterIterator<T extends PlotterData> {
    void accept(T data);

    @SuppressWarnings("unchecked")
    default void _accept(PlotterData data) {
        accept((T) data);
    }

    default PlotterIterator<T> andThen(PlotterIterator<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> { accept(t); after._accept(t); };
    }
}
