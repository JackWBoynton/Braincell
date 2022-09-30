package bottomtextdanny.braincell.libraries.gui_screen;

import net.minecraft.client.gui.screens.Screen;

import javax.annotation.Nullable;
import java.util.List;

public interface BCSelector {

    void setSelected(BCSelectable selectable);

    default void changeSelected(@Nullable BCSelectable selectable) {
        if (selected() != selectable) {
            if (selected() != null) {
                selected().onDeselection();

            }

            if (selectable != null) {
                setSelected(selectable);
                selectable.onSelection();

            } else {
                setSelected(null);
            }
        }
    }

    default void handleSelection(double mouseX, double mouseY, List<BCSelectable> selectables) {
        BCSelectable selected = selected();
        boolean yesSelection = selected != null;
        boolean shouldSelect = yesSelection && selected.shouldBeSelected((int)mouseX, (int)mouseY);
        boolean shouldDeselect = yesSelection && selected.shouldBeDeselected((int)mouseX, (int)mouseY);
        BCSelectable newSelected = selected;

        if (yesSelection && shouldSelect && !shouldDeselect) {
            return;
        }

        for (BCSelectable listener : selectables) {
            if (listener != selected && listener.shouldBeSelected((int) mouseX, (int) mouseY)) {
                newSelected = listener;
                listener.onSelection();
                if (yesSelection) {
                    selected.onDeselection();
                }
                break;
            }
        }

        if (yesSelection && newSelected == selected && shouldDeselect) {
            selected.onDeselection();
            newSelected = null;
        }
        setSelected(newSelected);
        if (this instanceof Screen screen) {
            screen.setFocused(newSelected);
        }
    }

    BCSelectable selected();
}
