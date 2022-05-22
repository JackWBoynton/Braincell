package bottomtextdanny.braincell.base;

import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.random.RandomGenerator;

public class Chooser<T> {
    private final NavigableMap<Float, T> weightTable;
    private final float total;

    private Chooser(NavigableMap<Float, T> weightTable, float total) {
        super();
        this.weightTable = weightTable;
        this.total = total;
    }

    public Chooser(NavigableMap<Float, T> weightTable) {
        super();
        float total = 0;

        for (Float weight : weightTable.keySet()) {
            total += weight;
        }

        this.weightTable = weightTable;
        this.total = total;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public T pick(RandomGenerator chooser) {
        return weightTable.higherEntry(total * chooser.nextFloat()).getValue();
    }

    public void fillArray(T[] array, RandomGenerator chooser) {
        for (int i = 0; i < array.length; i++) {
            array[i] = weightTable.higherEntry(total * chooser.nextFloat()).getValue();
        }
    }

    public static final class Builder<T> {
        private final NavigableMap<Float, T> weightTable;
        private float total;

        private Builder() {
            weightTable = new TreeMap<>();
        }

        public Builder<T> put(float weight, T item) {
            if (weight <= 0) return this;

            total += weight;
            weightTable.put(total, item);

            return this;
        }

        public Chooser<T> build() {
            return new Chooser<>(weightTable, total);
        }
    }
}
