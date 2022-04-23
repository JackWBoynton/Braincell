package net.bottomtextdanny.braincell.mod._base.plotter.iterator_support;

import net.bottomtextdanny.braincell.mod._base.plotter.iterator.PlotterData;
import net.bottomtextdanny.braincell.mod._base.plotter.iterator.PlotterIterator;

import java.util.Objects;
import java.util.function.Predicate;

public final class IfElsePlotterIterator<DATA extends PlotterData> implements PlotterIterator<DATA> {
    private final Predicate<PlotterData> predicate;
    private final PlotterIterator<? super DATA> happened;
    private PlotterIterator<? super DATA> didntHappen;

    public IfElsePlotterIterator(Predicate<PlotterData> predicate,
                                 PlotterIterator<? super DATA> happened) {
        this.predicate = predicate;
        this.happened = happened;
    }

    @Override
    public void accept(PlotterData data) {
        if (this.predicate.test(data)) {
            this.happened._accept(data);
        } else if (this.didntHappen != null) {
            this.didntHappen._accept(data);
        }
    }

    public PlotterIterator<DATA> orElse(PlotterIterator<? super DATA> actor) {
        if (this.didntHappen == null) this.didntHappen = actor;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (IfElsePlotterIterator<?>) obj;
        return Objects.equals(this.predicate, that.predicate) &&
                Objects.equals(this.happened, that.happened) &&
                Objects.equals(this.didntHappen, that.didntHappen);
    }

    @Override
    public int hashCode() {
        return Objects.hash(predicate, happened, didntHappen);
    }

    @Override
    public String toString() {
        return "EitherPlotterIter[" +
                "predicate=" + predicate + ", " +
                "happened=" + happened + ", " +
                "didntHappen=" + didntHappen + ']';
    }
}
