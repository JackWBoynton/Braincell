package bottomtextdanny.braincell.libraries.gui_screen;

import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;

public interface BCSelectable extends Widget, GuiEventListener {
    void attachToSelector(BCSelector selector);

    BCSelector selector();

    default boolean shouldBeSelected(int x, int y) {
        return false;
    }

    default boolean shouldBeDeselected(int x, int y) {
        return false;
    }

    default void onSelection() {}

    default void onDeselection() {}

    default boolean isSelected() {
        return this.selector() != null && this.selector().selected() == this;
    }
}
