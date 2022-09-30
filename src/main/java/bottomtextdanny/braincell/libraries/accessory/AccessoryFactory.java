package bottomtextdanny.braincell.libraries.accessory;

import net.minecraft.world.entity.player.Player;

@FunctionalInterface
public interface AccessoryFactory<E extends IAccessory> {

    E make(AccessoryKey<E> key, Player player);
}
