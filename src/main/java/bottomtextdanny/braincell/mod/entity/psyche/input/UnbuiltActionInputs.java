package bottomtextdanny.braincell.mod.entity.psyche.input;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;

public final class UnbuiltActionInputs {
   private final Map inputMap = Maps.newIdentityHashMap();

   public void put(ActionInputKey inputKey, Object input) {
      this.inputMap.put(inputKey, input);
   }

   public Map getInputMap() {
      return ImmutableMap.copyOf(this.inputMap);
   }
}
