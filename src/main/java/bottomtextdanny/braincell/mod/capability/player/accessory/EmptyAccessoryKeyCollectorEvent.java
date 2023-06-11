package bottomtextdanny.braincell.mod.capability.player.accessory;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import net.minecraftforge.eventbus.api.Event;

public class EmptyAccessoryKeyCollectorEvent extends Event {
   private final LinkedList externalEmptyKeys = Lists.newLinkedList();

   public boolean addKey(AccessoryKey key) {
      return this.externalEmptyKeys.add(key);
   }

   public Collection getCollection() {
      return Collections.unmodifiableList(this.externalEmptyKeys);
   }
}
