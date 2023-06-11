package bottomtextdanny.braincell.mod._mod.client_sided.events;

import bottomtextdanny.braincell.mod._mod.client_sided.ShaderHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;

@OnlyIn(Dist.CLIENT)
public class PostProcessingInitEvent extends Event {
   private final ShaderHandler handler;

   public PostProcessingInitEvent(ShaderHandler handler) {
      this.handler = handler;
   }

   public ShaderHandler getHandler() {
      return this.handler;
   }
}
