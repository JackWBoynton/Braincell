package bottomtextdanny.braincell.mod.capability.player.accessory.extensions;

import org.apache.commons.lang3.mutable.MutableFloat;

public interface FinnFall {
   int FALL_PRIORITY_HIGH = 0;
   int FALL_PRIORITY_MED = 10;
   int FALL_PRIORITY_LOW = 20;
   int FALL_CANCEL_PRIORITY = 200;

   void fallDamageMultModifier(float var1, MutableFloat var2, MutableFloat var3);

   int fallModificationPriority();
}
