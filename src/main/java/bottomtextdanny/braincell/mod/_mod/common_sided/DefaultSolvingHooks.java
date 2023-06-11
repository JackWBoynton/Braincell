package bottomtextdanny.braincell.mod._mod.common_sided;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.mod._base.registry.block_extensions.BCBlockSolvingHook;
import bottomtextdanny.braincell.mod._base.registry.item_extensions.ExtraItemModelLoader;
import bottomtextdanny.braincell.mod._base.registry.managing.DeferrorType;
import bottomtextdanny.braincell.mod.network.Connection;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;

public final class DefaultSolvingHooks {
   private final Map hooks = Maps.newIdentityHashMap();

   public DefaultSolvingHooks() {
      this.addHook(DeferrorType.BLOCK, new BCBlockSolvingHook());
      Connection.doClientSide(() -> {
         this.addHook(DeferrorType.ITEM, (item, solving) -> {
            if (item instanceof ExtraItemModelLoader modelLoader) {
               Braincell.client().getExtraModelLoaders().addModelLoader(modelLoader);
            }

         });
      });
   }

   private void addHook(DeferrorType type, SolvingHook hook) {
      if (this.hooks.containsKey(type)) {
         this.hooks.put(type, ((SolvingHook)this.hooks.get(type)).append(hook));
      } else {
         this.hooks.put(type, hook);
      }

   }

   @Nullable
   public SolvingHook getHook(DeferrorType type) {
      return this.hooks.containsKey(type) ? (SolvingHook)this.hooks.get(type) : null;
   }
}
