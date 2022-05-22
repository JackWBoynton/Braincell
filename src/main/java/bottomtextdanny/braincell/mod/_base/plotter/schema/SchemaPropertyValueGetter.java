package bottomtextdanny.braincell.mod._base.plotter.schema;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.List;

public final class SchemaPropertyValueGetter<T extends Comparable<T>> {
    private final Property<T> property;
    private final List<Object> values;
    private final boolean invalid;

    public SchemaPropertyValueGetter(Property<T> property, List<Object> values) {
        boolean invalid = false;
        for (Object val : values) {
            if (!property.getValueClass().isAssignableFrom(val.getClass())) {
                invalid = true;

            }
        }
        if (property == null) invalid = true;

        this.values = invalid ? null : values;
        this.property = property;
        this.invalid = invalid;
    }

    public Property<T> getProperty() {
        return property;
    }

    public BlockState tryInferValue(int valueIndex, BlockState blockState) {
        if (!invalid && blockState.hasProperty(this.property)) {
            blockState = blockState.setValue(this.property, getValue(valueIndex));
        }

        return blockState;
    }

    @SuppressWarnings("unchecked")
    public T getValue(int index) {
        return invalid ? null : (T)values.get(index);
    }
}
