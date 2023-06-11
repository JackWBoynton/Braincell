package bottomtextdanny.braincell.mod._base.registry;

import bottomtextdanny.braincell.mod.world.builtin_items.BCSpawnEggItem;

public record EggBuildData(String name, BCSpawnEggItem.Builder builder) {
   public EggBuildData(String name, BCSpawnEggItem.Builder builder) {
      this.name = name;
      this.builder = builder;
   }

   public String name() {
      return this.name;
   }

   public BCSpawnEggItem.Builder builder() {
      return this.builder;
   }
}
