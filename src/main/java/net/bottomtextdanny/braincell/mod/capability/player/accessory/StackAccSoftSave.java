package net.bottomtextdanny.braincell.mod.capability.player.accessory;

import net.bottomtextdanny.braincell.base.ObjectFetcher;
import net.minecraft.world.level.ItemLike;

public interface StackAccSoftSave extends ItemLike {
	Object[] save();
	
	void retrieve(ObjectFetcher save);
}
