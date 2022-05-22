package bottomtextdanny.braincell.mod.gui;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.base.screen.ImageData;
import bottomtextdanny.braincell.mod._base.blitty.Blitty;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.apache.commons.compress.utils.Lists;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class ListingBox extends EditableText {
    private static final ImageData BUTTONS_IMAGE = new ImageData(Braincell.ID, "textures/gui/screen/listing_box_buttons.png", 32, 32);
    private static final Blitty[] ARROW_UP = new Blitty[] {
            new Blitty(BUTTONS_IMAGE, 0, 0, 16, 8),
            new Blitty(BUTTONS_IMAGE, 0, 16, 16, 8)
    };
    private static final Blitty[] ARROW_DOWN = new Blitty[] {
            new Blitty(BUTTONS_IMAGE, 0, 8, 16, 8),
            new Blitty(BUTTONS_IMAGE, 0, 24, 16, 8)
    };
    private static final Blitty[] DELETE = new Blitty[] {
            new Blitty(BUTTONS_IMAGE, 16, 0, 16, 16),
            new Blitty(BUTTONS_IMAGE, 16, 16, 16, 16)
    };
    private static int LIST_Y_OFFSET = 16;
    private static int ENTRY_HEIGHT = 16;
    private static int ENTRY_HEIGHT_DIV2 = ENTRY_HEIGHT / 2;
    private static int MAX_DISPLAY_ENTRIES = 5;
    private static int ENTRIES_HEIGHT = ENTRY_HEIGHT * MAX_DISPLAY_ENTRIES;
    private static int ENTRY_CONTROLLERS_WIDTH = 16;
    private static int BORDER_WIDTH = 1;
    private static int ENTRY_SPACING = 2;
    private List<String> contents;
    @Nullable
    private final Set<String> entriesSet;
    private Predicate<String> validator;
    private int contentDisplayOffset;
    private int listBackgroundColor;
    private int entryBackgroundColor;
    private int entryColor;

    public ListingBox(Font font, int x, int y, int width, Component message, boolean allowsDuplicates) {
        super(font, x, y, width, message);
        setValidator(s -> true);
        this.contents = Lists.newArrayList();
        this.entriesSet = allowsDuplicates ?
                null:
                Sets.newHashSet();
        this.listBackgroundColor = 0xFF000000;
        this.entryBackgroundColor = 0xFF202020;
        this.entryColor = 0xFFFFFFFF;
    }

    @Override
    public void render(PoseStack pose, int x, int y, float tickOffset) {
        super.render(pose, x, y, tickOffset);
        if (!isSelected()) return;

        int listSize = this.contents.size();
        int listY = listY();

        int entryX = this.x + BORDER_WIDTH;
        int entryWidth = this.width - ENTRY_CONTROLLERS_WIDTH * 2 - BORDER_WIDTH * 2;
        int firstIndex = fistVisibleIndex();
        int displayLength = listSize - firstIndex;
        int displayListLength = Math.min(displayLength, MAX_DISPLAY_ENTRIES);

        int backgroundHeight = Mth.clamp(displayListLength, 1, MAX_DISPLAY_ENTRIES) * ENTRY_HEIGHT + BORDER_WIDTH * 2;

        fill(pose, this.x, listY, this.x + this.width, listY + backgroundHeight, getBorderColor());
        fill(pose, this.x + 1, listY + 1, this.x + this.width - 1, listY + backgroundHeight - 1, this.listBackgroundColor);

        int lineHeightDiv2 = font().lineHeight / 2;
        String entryMessage;
        int localOffsetY = BORDER_WIDTH;

        if (displayListLength == 0) {
            entryMessage = "<no entries>";
            entryMessage = font().plainSubstrByWidth(entryMessage, entryWidth - ENTRY_SPACING * 2);

            font().drawShadow(pose, entryMessage,
                    entryX + ENTRY_SPACING,
                    listY + localOffsetY + ENTRY_HEIGHT_DIV2 - lineHeightDiv2,
                    this.entryColor);
            return;
        }


        int entryY;
        int localX;
        int localY;
        Blitty delete;
        Blitty up;
        Blitty down;

        for (int i = 0; i < displayListLength; i++) {
            up = ARROW_UP[0];
            down = ARROW_DOWN[0];
            delete = DELETE[0];
            entryY = listY + localOffsetY;
            fill(pose, entryX, entryY, entryX + entryWidth, entryY + ENTRY_HEIGHT, 0xFF000000);
            fill(pose, entryX + 1, entryY + 1, entryX + entryWidth - 1, entryY + ENTRY_HEIGHT - 1, this.entryBackgroundColor);

            localX = x - this.x;
            localY = y - entryY;

            if (x >= this.x && x < this.x + this.width
                    && y >= entryY && y < entryY + ENTRY_HEIGHT) {
                if (touchesEntryUp(localX, localY)) {
                    up = ARROW_UP[1];
                } else if (touchesEntryDown(localX, localY)) {
                    down = ARROW_DOWN[1];
                } else if (touchesEntryDelete(localX, localY)) {
                    delete = DELETE[1];
                }
            }

            BUTTONS_IMAGE.use();
            up.render(pose, entryX + entryWidth, entryY, 0.0F);
            down.render(pose, entryX + entryWidth, entryY + ENTRY_HEIGHT_DIV2, 0.0F);
            delete.render(pose, entryX + entryWidth + ENTRY_CONTROLLERS_WIDTH, entryY, 0.0F);

            entryMessage = this.contents.get(firstIndex + i);
            entryMessage = font().plainSubstrByWidth(entryMessage, entryWidth - ENTRY_SPACING * 2);

            font().drawShadow(pose, entryMessage, entryX + ENTRY_SPACING, entryY + ENTRY_HEIGHT_DIV2 - lineHeightDiv2, this.entryColor);

            localOffsetY += ENTRY_HEIGHT;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isSelected()) {
            int contentsY = listY();
            if (mouseX >= this.x && mouseX < this.x + this.width
                    && mouseY >= contentsY && mouseY < contentsY + ENTRIES_HEIGHT) {
                int offset = (int)((mouseY - contentsY) % ENTRY_HEIGHT);
                int index = offset + fistVisibleIndex();

                if (index < this.contents.size()) {
                    int localX = (int) mouseX - this.x;
                    int localY = ((int) mouseY - contentsY) % ENTRY_HEIGHT;
                    SoundManager m = Minecraft.getInstance().getSoundManager();

                    if (touchesEntryDelete(localX, localY)) {
                        if (tryDeleteEntry(index)) playDownSound(m);
                    } else if (touchesEntryUp(localX, localY)) {
                        if (tryMoveEntryUp(index)) playDownSound(m);
                    } else if (touchesEntryDown(localX, localY)) {
                        if (tryMoveEntryDown(index)) playDownSound(m);
                    }
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double step) {
        int contentsY = listY();
        if (mouseX >= this.x && mouseX < this.x + this.width
                && mouseY >= contentsY && mouseY < contentsY + ENTRIES_HEIGHT) {

            setContentDisplayOffset(this.contentDisplayOffset - (int)(step));
        }
        return super.mouseScrolled(mouseX, mouseY, step);
    }

    @Override
    public boolean keyPressed(int key, int p_94746_, int p_94747_) {
        if ((key == InputConstants.KEY_RETURN || key == InputConstants.KEY_NUMPADENTER)
                && this.validator.test(getCurrentText())) {
            addEntry(getCurrentText());
            return true;
        }
        return super.keyPressed(key, p_94746_, p_94747_);
    }

    public boolean tryDeleteEntry(int index) {
        if (index >= 0 && index < this.contents.size()) {
            if (!allowsDuplicates()) {
                this.entriesSet.remove(this.contents.get(index));
            }
            this.contents.remove(index);
            return true;
        }
        return false;
    }

    public boolean tryMoveEntryUp(int index) {
        if (index >= 1 && index < this.contents.size()) {
            String temp = this.contents.get(index - 1);
            this.contents.set(index - 1, this.contents.get(index));
            this.contents.set(index, temp);
            return true;
        }
        return false;
    }

    public boolean tryMoveEntryDown(int index) {
        if (index >= 0 && index < this.contents.size() - 1) {
            String temp = this.contents.get(index + 1);
            this.contents.set(index + 1, this.contents.get(index));
            this.contents.set(index, temp);
            return true;
        }
        return false;
    }

    public void addEntry(String string) {
        if (!allowsDuplicates()) {
            if (!this.entriesSet.contains(string)) {
                this.entriesSet.add(string);
                this.contents.add(string);
            }
        } else {
            this.contents.add(string);
        }
    }

    public void setNewContents(List<String> contents) {
        this.contents = contents;
        if (!allowsDuplicates()) {
            this.entriesSet.clear();
            List<String> strings = this.contents;
            for (int i = 0; i < strings.size(); i++) {
                String c = strings.get(i);
                if (this.entriesSet.contains(c)) {
                    this.contents.remove(i);
                    i--;
                } else {
                    this.entriesSet.add(c);
                }
            }
        }
    }

    public void setValidator(Predicate<String> validator) {
        this.validator = validator;
    }

    public void setContentDisplayOffset(int value) {
        this.contentDisplayOffset = Math.max(0, Math.min(value, this.contents.size() - MAX_DISPLAY_ENTRIES));
    }

    public void setListBackgroundColor(int listBackgroundColor) {
        this.listBackgroundColor = listBackgroundColor;
    }

    public void setEntryBackgroundColor(int entryBackgroundColor) {
        this.entryBackgroundColor = entryBackgroundColor;
    }

    public void setEntryColor(int entryColor) {
        this.entryColor = entryColor;
    }

    public List<String> getContents() {
        return this.contents;
    }

    private boolean touchesEntryDelete(int localX, int localY) {
        return localX >= this.width - ENTRY_CONTROLLERS_WIDTH;
    }

    private boolean touchesEntryUp(int localX, int localY) {
        return localX >= this.width - ENTRY_CONTROLLERS_WIDTH * 2
                && localX < this.width - ENTRY_CONTROLLERS_WIDTH
                && localY < ENTRY_CONTROLLERS_WIDTH / 2;
    }

    private boolean touchesEntryDown(int localX, int localY) {
        return localX >= this.width - ENTRY_CONTROLLERS_WIDTH * 2
                && localX < this.width - ENTRY_CONTROLLERS_WIDTH
                && localY >= ENTRY_CONTROLLERS_WIDTH / 2;
    }

    private int listY() {
        return this.y + 15;
    }

    private int fistVisibleIndex() {
        return this.contentDisplayOffset;
    }

    public boolean isDuplicate(String string) {
        return !allowsDuplicates() && this.entriesSet.contains(string);
    }

    public boolean allowsDuplicates() {
        return this.entriesSet == null;
    }
}
