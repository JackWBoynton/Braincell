package bottomtextdanny.braincell.mod.capability.player.accessory;

import net.minecraft.resources.ResourceLocation;

public final class BCAccessoryKeys {
   public static final AccessoryKey EMPTY = create("empty", (player, factory) -> {
      return new IAccessory.EmptyAccessory();
   });
   public static final AccessoryKey STACK_EMPTY = create("stack_empty", (player, factory) -> {
      return StackAccessory.EMPTY;
   });

   public static AccessoryKey create(String name, AccessoryFactory factory) {
      return AccessoryKey.createKey(new ResourceLocation("braincell", name), factory);
   }

   public static void loadClass() {
   }
}
