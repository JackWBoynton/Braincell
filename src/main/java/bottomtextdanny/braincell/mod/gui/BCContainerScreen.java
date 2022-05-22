package bottomtextdanny.braincell.mod.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BCContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> implements BCSelector {
    private final List<Widget> renderableSelectables;
    private final List<BCSelectable> selectables;
    @Nullable
    private BCSelectable selected;

    public BCContainerScreen(T menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        renderableSelectables = Lists.newArrayList();
        selectables = Lists.newArrayList();
    }

    @Override
    protected void init() {
        super.init();
        this.selectables.clear();
        this.renderableSelectables.clear();
        this.selected = null;
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        for(BCSelectable widget : selectables) {
            if (widget instanceof TickableComponent tickable) {
                tickable.tick();
            }
        }
    }

    @Override
    public void setFocused(@Nullable GuiEventListener listener) {
        if (listener instanceof BCSelectable selectable) {
            changeSelected(selectable);
            return;
        }
        changeSelected(null);
        super.setFocused(listener);
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickOffset) {
        super.render(pose, mouseX, mouseY, tickOffset);

        for(Widget widget : this.renderableSelectables) {
            if (widget != this.selected) {
                widget.render(pose, mouseX, mouseY, tickOffset);
            }
        }

        if (this.selected != null) {
            selected.render(pose, mouseX, mouseY, tickOffset);
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double step) {
        if (selected != null && selected.mouseScrolled(mouseX, mouseY, step))
            return true;
        return this.getFocused() != null && this.getFocused().mouseScrolled(mouseX, mouseY, step);
    }

    @Override
    public boolean mouseDragged(double var1, double var2, int var3, double var4, double var5) {
        if (selected != null && selected.mouseDragged(var1, var2, var3, var4, var5))
            return true;
        return super.mouseDragged(var1, var2, var3, var4, var5);
    }

    @Override
    public boolean keyPressed(int var1, int var2, int var3) {
        if (selected != null) {

            if (selected.keyPressed(var1, var2, var3))
                return true;
            else if (Minecraft.getInstance().options.keyInventory.matches(var1, var2))
                return false;
        }
        return super.keyPressed(var1, var2, var3);
    }

    @Override
    public boolean keyReleased(int var1, int var2, int var3) {
        if (selected != null && selected.keyReleased(var1, var2, var3))
            return true;
        return super.keyReleased(var1, var2, var3);
    }

    @Override
    public boolean charTyped(char var1, int var2) {
        if (selected != null) {
            if (selected.charTyped(var1, var2))
                return true;
        }
        return super.charTyped(var1, var2);
    }

    @Override
    protected <U extends GuiEventListener & Widget & NarratableEntry> U addRenderableWidget(U widget) {
        if (widget instanceof BCSelectable) {
            this.renderableSelectables.add(widget);
            return this.addWidget(widget);
        }
        return super.addRenderableWidget(widget);
    }

    @Override
    protected <U extends GuiEventListener & NarratableEntry> U addWidget(U widget) {
        if (widget instanceof BCSelectable bcLisntener) {
            this.selectables.add(bcLisntener);
            bcLisntener.attachToSelector(this);
        }
        return super.addWidget(widget);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mod) {
        handleSelection(mouseX, mouseY, this.selectables);
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
