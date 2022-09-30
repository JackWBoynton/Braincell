package bottomtextdanny.braincell.libraries.gui_screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.List;

public class BCScreen extends Screen implements BCSelector {
    private final List<BCSelectable> bcListenerList;
    @Nullable
    private BCSelectable selected;

    protected BCScreen(Component title) {
        super(title);
        this.bcListenerList = Lists.newLinkedList();
    }

    @Override
    protected void init() {
        super.init();
        this.bcListenerList.clear();
        this.selected = null;
    }

    public void resize(Minecraft minecraft, int width, int height) {
        this.init(minecraft, width, height);
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickOffset) {
        for(Widget widget : this.renderables) {
            if (widget != this.selected) {
                widget.render(pose, mouseX, mouseY, tickOffset);
            }
        }

        if (this.selected != null) {
            selected.render(pose, mouseX, mouseY, tickOffset);
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double step) {
        return this.getFocused() != null && this.getFocused().mouseScrolled(mouseX, mouseY, step);
    }

    @Override
    protected <T extends GuiEventListener & NarratableEntry> T addWidget(T widget) {
        if (widget instanceof BCSelectable bcLisntener) {
            this.bcListenerList.add(bcLisntener);
            bcLisntener.attachToSelector(this);
        }
        return super.addWidget(widget);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mod) {
        handleSelection(mouseX, mouseY, this.bcListenerList);
        return super.mouseClicked(mouseX, mouseY, mod);
    }

    @Override
    public BCSelectable selected() {
        return this.selected;
    }

    @Override
    public void setSelected(BCSelectable selectable) {
        this.selected = selectable;
        setFocused(selectable);
    }
}
