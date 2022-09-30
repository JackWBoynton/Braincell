package bottomtextdanny.braincell.events;

import net.minecraftforge.eventbus.api.Event;

public class ResizeEvent extends Event {
    public final int fboWidth, fboHeight;

    public ResizeEvent(int width, int height) {
        this.fboWidth = width;
        this.fboHeight = height;
    }
}
