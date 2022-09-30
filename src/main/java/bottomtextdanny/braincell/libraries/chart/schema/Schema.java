package bottomtextdanny.braincell.libraries.chart.schema;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.List;

public final class Schema {
    public static final Schema DUMMY = new Schema(List.of(), List.of(), List.of(), List.of(), IntList.of(), new BoundingBox(0, 0, 0, 0, 0, 0));
    private final List<SchemaPropertyValueGetter<?>> possiblePropertiesMatrix;
    private final List<FlagsEntry> entries;
    private final List<BlockPos> blockPositions;
    private final List<IntList> propertyValueIndices;
    private final IntList entryIndices;
    private final BoundingBox builtinBox;

    public Schema(List<SchemaPropertyValueGetter<?>> possiblePropertiesMatrix,
                  List<FlagsEntry> entries, List<BlockPos> blockPositions,
                  List<IntList> propertyValueIndices, IntList entryIndices,
                  BoundingBox builtinBox) {
        this.possiblePropertiesMatrix = possiblePropertiesMatrix;
        this.entries = entries;
        this.blockPositions = blockPositions;
        this.propertyValueIndices = propertyValueIndices;
        this.entryIndices = entryIndices;
        this.builtinBox = builtinBox;
    }

    public SchemaPropertyValueGetter<?> getPropertyValues(int index) {
        return possiblePropertiesMatrix.get(index);
    }

    public BlockState tryInferPropertyValue(int propertyIndex, int valueIndex, BlockState blockState) {
        SchemaPropertyValueGetter<?> property = getPropertyValues(propertyIndex);
        return property.tryInferValue(valueIndex, blockState);
    }

    public void iterate(TriConsumer<BlockPos, IntList, FlagsEntry> cons) {
        List<BlockPos> blockPositions = this.blockPositions;
        List<IntList> propertyValueIndices = this.propertyValueIndices;
        List<FlagsEntry> entries = this.entries;
        IntList entryIndices = this.entryIndices;
        for (int i = 0; i < blockPositions.size(); i++) {
            cons.accept(blockPositions.get(i), propertyValueIndices.get(i), entries.get(entryIndices.getInt(i)));
        }
    }

    public BoundingBox getBuiltinBox() {
        return builtinBox;
    }
}
