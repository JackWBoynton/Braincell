package bottomtextdanny.braincell.libraries.accessory;

import bottomtextdanny.braincell.Braincell;
import net.minecraft.resources.ResourceLocation;

public final class BCAccessoryKeys {
    public static final AccessoryKey<IAccessory.EmptyAccessory> EMPTY = create("empty", (player, factory) -> new IAccessory.EmptyAccessory());
    public static final AccessoryKey<StackAccessory> STACK_EMPTY = create("stack_empty", (player, factory) -> StackAccessory.EMPTY);

    public static <E extends IAccessory> AccessoryKey<E> create(String name, AccessoryFactory<E> factory) {
        return AccessoryKey.createKey(new ResourceLocation(Braincell.ID, name), factory);
    }

    public static void loadClass() {}
}
