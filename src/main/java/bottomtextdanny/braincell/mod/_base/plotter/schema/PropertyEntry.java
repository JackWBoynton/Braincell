package bottomtextdanny.braincell.mod._base.plotter.schema;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public record PropertyEntry(Property property, Comparable value) {
   public PropertyEntry(Property property, Comparable value) {
      this.property = property;
      this.value = value;
   }

   public BlockState tryInfer(BlockState blockState) {
      return blockState.hasProperty(this.property) ? (BlockState)blockState.setValue(this.property, this.value) : blockState;
   }

   public Property property() {
      return this.property;
   }

   public Comparable value() {
      return this.value;
   }
}
