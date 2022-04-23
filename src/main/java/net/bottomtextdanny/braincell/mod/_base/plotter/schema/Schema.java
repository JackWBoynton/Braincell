package net.bottomtextdanny.braincell.mod._base.plotter.schema;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.List;

public final class Schema {
    public static final Schema DUMMY = new Schema(List.of(), List.of(), List.of(), List.of(), IntList.of());
    private final List<SchemaPropertyValueGetter<?>> possiblePropertiesMatrix;
    private final List<FlagsEntry> entries;
    private final List<BlockPos> blockPositions;
    private final List<IntList> propertyValueIndices;
    private final IntList entryIndices;

    public Schema(List<SchemaPropertyValueGetter<?>> possiblePropertiesMatrix,
                  List<FlagsEntry> entries, List<BlockPos> blockPositions,
                  List<IntList> propertyValueIndices, IntList entryIndices) {
        this.possiblePropertiesMatrix = possiblePropertiesMatrix;
        this.entries = entries;
        this.blockPositions = blockPositions;
        this.propertyValueIndices = propertyValueIndices;
        this.entryIndices = entryIndices;
    }

    public SchemaPropertyValueGetter<?> getPropertyValues(int index) {
        return possiblePropertiesMatrix.get(index);
    }

    public BlockState tryInferPropertyValue(int propertyIndex, int valueIndex, BlockState blockState) {
        SchemaPropertyValueGetter<?> property = getPropertyValues(propertyIndex);
        return property.tryInferValue(valueIndex, blockState);
    }

    public void iterate(TriConsumer<BlockPos, IntList, FlagsEntry> cons) {
        for (int i = 0; i < this.blockPositions.size(); i++) {
            cons.accept(this.blockPositions.get(i), this.propertyValueIndices.get(i), this.entries.get(this.entryIndices.getInt(i)));
        }
    }
}
