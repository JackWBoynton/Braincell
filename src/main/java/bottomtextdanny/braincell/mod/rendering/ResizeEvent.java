package bottomtextdanny.braincell.mod.rendering;

import net.minecraftforge.eventbus.api.Event;

public class ResizeEvent extends Event {
   public final int fboWidth;
   public final int fboHeight;

   public ResizeEvent(int width, int height) {
      this.fboWidth = width;
      this.fboHeight = height;
   }
}
