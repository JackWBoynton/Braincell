package bottomtextdanny.braincell.mod.capability.player.accessory;

import bottomtextdanny.braincell.base.ObjectFetcher;
import net.minecraft.world.level.ItemLike;

public interface StackAccSoftSave extends ItemLike {
   Object[] save();

   void retrieve(ObjectFetcher var1);
}
