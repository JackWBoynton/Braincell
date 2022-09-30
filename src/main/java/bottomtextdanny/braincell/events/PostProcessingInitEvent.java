package bottomtextdanny.braincell.events;

import bottomtextdanny.braincell.libraries.shader.ShaderHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;

@OnlyIn(Dist.CLIENT)
public class PostProcessingInitEvent extends Event {
    private final ShaderHandler handler;

    public PostProcessingInitEvent(ShaderHandler handler) {
        super();
        this.handler = handler;
    }

    public ShaderHandler getHandler() {
        return handler;
    }
}
