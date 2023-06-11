package bottomtextdanny.braincell.mod._base.registry.item_extensions;

import net.minecraft.client.resources.model.ModelResourceLocation;

public record ExtraModelData(int index, ModelResourceLocation location) {
   public ExtraModelData(int index, ModelResourceLocation location) {
      this.index = index;
      this.location = location;
   }

   public static ExtraModelData of(Enum entry, ModelResourceLocation location) {
      return new ExtraModelData(entry.ordinal(), location);
   }

   public static ExtraModelData of(int index, ModelResourceLocation location) {
      return new ExtraModelData(index, location);
   }

   public int index() {
      return this.index;
   }

   public ModelResourceLocation location() {
      return this.location;
   }
}
