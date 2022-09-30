package bottomtextdanny.braincell.libraries.chart.schema;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public record PropertyEntry<T extends Comparable<T>>(Property<T> property, T value) {
    public BlockState tryInfer(BlockState blockState) {
        return blockState.hasProperty(this.property) ? blockState.setValue(this.property, this.value) : blockState;
    }
}
