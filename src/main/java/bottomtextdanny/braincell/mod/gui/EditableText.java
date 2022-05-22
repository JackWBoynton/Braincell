package bottomtextdanny.braincell.mod.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import bottomtextdanny.braincell.base.BCCharacterUtil;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class EditableText extends AbstractWidget implements BCSelectable, TickableComponent {
    private Function<String, String> textProcessor = Function.identity();
    private Predicate<Character> wordSeparator;
    private Function<String, Integer> colorProcessor;
    private Consumer<String> onTextModification;
    private BiConsumer<String, Integer> keyActor = (text, keyCode) -> {};
    private final Font font;
    private final int underscoreSpace;
    private BCSelector selector;
    private String currentText;
    private int tickCounter;
    private int borderColor, unfocusedBorderColor;
    //global position
    private int offset, highlight, pointer;

    public EditableText(Font font, int x, int y, int width, Component message) {
        super(x, y, width, 16, message);
        this.font = font;
        this.currentText = "";
        setColorProcessor((text) -> 0xFFFFFFFF);
        setWordSeparator(BCCharacterUtil.wordSeparator());
        this.underscoreSpace = this.font.width("_");
        onTextModification = text -> {};
    }

    @Override
    public void render(PoseStack pose, int x, int y, float tickOffset) {
        super.render(pose, x, y, tickOffset);
    }

    @Override
    public void tick() {
        this.tickCounter++;
    }

    @Override
    public void renderButton(PoseStack pose, int x, int y, float tickOffset) {
        boolean selected = isSelected();
        boolean highlighting = highlightActive();
        boolean showMarker = selected && Math.abs(this.tickCounter % 10) <= 5;
        String pointerMarker = "|";

        int borderColor = selected ? this.borderColor : this.unfocusedBorderColor;
        int maxLength = getMaxShowLength();
        String offsetText = this.currentText.substring(this.offset);
        String displayedText = this.font.plainSubstrByWidth(offsetText, maxLength);
        int pointerDisplayIndex = Math.max(0, this.pointer - this.offset);
        int pointerX = this.font.width(offsetText.substring(0, pointerDisplayIndex));
        int clampedPointerX = Math.min(pointerX, maxLength);

        fill(pose, this.x, this.y, this.x + this.width, this.y + this.height, borderColor);
        fill(pose, this.x + 1, this.y + 1, this.x + this.width - 1, this.y + this.height - 1, 0xFF000000);

        if (highlighting) {
            int highlightDisplayIndex = Math.max(0, this.highlight - this.offset);
            int highlightX = Math.min(this.font.width(offsetText.substring(0, highlightDisplayIndex)), maxLength);
            int min = Math.min(pointerX, highlightX);
            int max = Math.max(pointerX, highlightX);

            fill(pose, this.x + min + 2, this.y + 3, this.x + max + 3, this.y + 13, 0x800020FF);
        }

        if (!displayedText.isEmpty()) {
            this.font.drawShadow(pose, displayedText, this.x + 3, this.y + 4, 0xFFFFFFFF);
        } else {
            String ghostText = this.font.plainSubstrByWidth(getMessage().getString(), maxLength);
            this.font.drawShadow(pose, ghostText, this.x + 3, this.y + 4, 0xFF606060);
        }

        if (!highlighting && this.pointer == this.currentText.length() && maxLength - pointerX >= this.underscoreSpace) {
            pointerMarker = "_";
        }

        if (showMarker) {
            this.font.drawShadow(pose, pointerMarker, this.x + clampedPointerX + 2, this.y + 4, 0xFFFFFFFF);
        }
    }

    public void updateNarration(NarrationElementOutput out) {
        out.add(NarratedElementType.TITLE, new TranslatableComponent("narration.edit_box", this.currentText));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isSelected() && inbounds(mouseX, mouseY)) {
            int lx = (int)mouseX - this.x;
            String offsetString = getOffsetString();

            setPointer(this.font.plainSubstrByWidth(offsetString, lx).length());
            return true;
        }
        return false;
    }

    @Override
    public boolean keyPressed(int key, int p_94746_, int p_94747_) {
        if (!isSelected()) return false;
        runKeyActor(key);
        boolean shiftDown = Screen.hasShiftDown();
        boolean controlDown = Screen.hasControlDown();
        if (Screen.isSelectAll(key)) {
            setPointer(this.currentText.length());
            setHighlight(0);
            return true;
        } else if (Screen.isCopy(key) && highlightActive()) {
            Minecraft.getInstance().keyboardHandler.setClipboard(this.getHighlighted());
            return true;
        } else if (Screen.isPaste(key)) {
            this.insertText(this.pointer, Minecraft.getInstance().keyboardHandler.getClipboard());
            return true;
        } else if (Screen.isCut(key) && highlightActive()) {
            Minecraft.getInstance().keyboardHandler.setClipboard(this.getHighlighted());
            setCurrentText("");
            return true;
        } else {
            switch (key) {
                case 259: //backspace
                    if (controlDown) {
                        deleteTextByIndices(getNextWordIndex(this.pointer, true), this.pointer);
                    } else {
                        if (highlightActive()) {
                            int min = Math.min(this.pointer, this.highlight);
                            int max = Math.max(this.pointer, this.highlight);
                            deleteTextByIndices(min, max);
                        } else {
                            deleteText(this.pointer, 1, true);
                        }
                    }
                    return true;
                case 260:
                case 264:
                case 265:
                case 266:
                case 267:
                case 261: //delete
                    if (controlDown) {
                        deleteTextByIndices(this.pointer, getNextWordIndex(this.pointer, false));
                    } else {
                        if (highlightActive()) {
                            int min = Math.min(this.pointer, this.highlight);
                            int max = Math.max(this.pointer, this.highlight);
                            deleteTextByIndices(min, max);
                        } else {
                            deleteText(this.pointer, 1, false);
                        }
                    }
                    return true;
                case 262: //right arrow?
                    int oldHighlight = this.highlight;
                    if (controlDown) {
                        if (shiftDown) {
                            this.setPointer(getNextWordIndex(this.pointer, false), false);
                            this.setHighlight(oldHighlight);
                        } else {
                            this.setPointer(getNextWordIndex(this.pointer, false), true);
                        }
                    } else {
                        if (shiftDown) {
                            this.setPointer(this.pointer + 1, false);
                            this.setHighlight(oldHighlight);
                        } else {
                            this.setPointer(Math.max(this.highlight, this.pointer + 1), true);
                        }
                    }
                    return true;
                case 263: //left arrow??
                    oldHighlight = this.highlight;
                    if (controlDown) {
                        if (shiftDown) {
                            this.setPointer(getNextWordIndex(this.pointer, true), false);
                            this.setHighlight(oldHighlight);
                        } else {
                            this.setPointer(getNextWordIndex(this.pointer, true), true);
                        }
                    } else {
                        if (shiftDown) {
                            this.setPointer(this.pointer - 1, false);
                            this.setHighlight(oldHighlight);
                        } else {
                            this.setPointer(Math.min(this.highlight, this.pointer - 1), true);
                        }
                    }
                    return true;
                case 268:
                    this.setPointer(0);
                    return true;
                case 269:
                    this.setPointer(this.currentText.length());
                    return true;
            }
        }
        return false;
    }

    public void runKeyActor(int key) {
        this.keyActor.accept(this.currentText, key);
    }

    public void runOnTextModification() {
        onTextModification.accept(currentText);
    }

    @Override
    public boolean charTyped(char character, int modifiers) {
        if (SharedConstants.isAllowedChatCharacter(character)) {
            this.insertText(this.pointer, Character.toString(character));
            return true;
        } else {
            return false;
        }
    }

    private void insertText(int at, String text) {
        if (text != null && !text.isEmpty() && at <= this.currentText.length()) {
            boolean movePointer = this.pointer >= at;
            String offsetBackwards = this.currentText.substring(0, at);
            String offsetForward = this.currentText.substring(at);
            String rawNewText = offsetBackwards + text + offsetForward;
            setCurrentText(this.textProcessor.apply(rawNewText));
            if (movePointer) {
                setPointer(this.pointer + text.length());
            }
        }
    }

    private void deleteText(int at, int characters, boolean backwards) {
        if (!this.currentText.isEmpty() && at <= this.currentText.length()) {
            boolean movePointer = this.pointer >= at;
            int deletionStart, deletionEnd;
            if (backwards) {
                deletionStart = Math.max(0, at - characters);
                deletionEnd = at;
            } else {
                deletionStart = at;
                deletionEnd = Math.min(this.currentText.length(), at + characters);
            }
            setCurrentText(this.currentText.substring(0, deletionStart) + this.currentText.substring(deletionEnd));
            if (movePointer) {
                setPointer(deletionStart);
            }
        }
    }

    private void deleteTextByIndices(int start, int end) {
        if (!this.currentText.isEmpty() && end <= this.currentText.length()) {
            boolean movePointer = this.pointer >= start;

            setCurrentText(this.currentText.substring(0, start) + this.currentText.substring(end));
            if (movePointer) {
                setPointer(start);
            }
        }
    }

    private void checkOffset() {
        int maxLength = getMaxShowLength();
        int lPointer = Mth.clamp(this.pointer -1, 0, this.currentText.length());
        if (lPointer < this.offset) {
            String textToPointer = this.currentText.substring(0, lPointer);

            String reverseFromPointer = new StringBuffer(textToPointer).reverse().toString();
            int cutoff = this.font.plainSubstrByWidth(reverseFromPointer, maxLength).length();
            setOffset(textToPointer.length() - cutoff);

        } else {
            String offsetText = this.currentText.substring(this.offset, this.pointer);
            int xLength = this.font.width(offsetText);

            if (xLength > maxLength) {
                String cutText = this.font.plainSubstrByWidth(offsetText, maxLength);
                int cutoff = offsetText.length() - cutText.length();
                setOffset(this.offset + cutoff);
            }
        }
    }

    public void updateColor() {
        this.borderColor = this.colorProcessor.apply(this.currentText);
        int a = (this.borderColor >> 24 & 255);
        int r = (this.borderColor >> 16 & 255) / 2;
        int g = (this.borderColor >> 8 & 255) / 2;
        int b = (this.borderColor & 255) / 2;
        this.unfocusedBorderColor = (a << 24) + (r << 16) + (g << 8) + b;
    }

    @Override
    public boolean shouldBeSelected(int x, int y) {
        return inbounds(x, y);
    }

    @Override
    public void onSelection() {
        playDownSound(Minecraft.getInstance().getSoundManager());
    }

    public void setCurrentText(String currentText) {
        this.currentText = currentText;
        if (this.pointer > this.currentText.length()) {
            setPointer(this.currentText.length());
        }
        setHighlight(this.pointer);
        checkOffset();
        runOnTextModification();
        updateColor();
    }

    public void setTextProcessor(Function<String, String> textProcessor) {
        this.textProcessor = textProcessor;
    }

    public void setKeyActor(BiConsumer<String, Integer> keyActor) {
        this.keyActor = keyActor;
    }

    public void setWordSeparator(Predicate<Character> wordSeparator) {
        this.wordSeparator = wordSeparator;
    }

    public void setColorProcessor(Function<String, Integer> colorProcessor) {
        this.colorProcessor = colorProcessor;
        updateColor();
    }

    public void onTextModification(Consumer<String> onTextModification) {
        this.onTextModification = onTextModification;
    }

    protected void setPointer(int pointer) {
        setPointer(pointer, true);
    }

    protected void setPointer(int pointer, boolean resetHighlight) {
        this.pointer = Math.max(0, Math.min(pointer, this.currentText.length()));
        if (resetHighlight) {
            this.highlight = this.pointer;
        }
        checkOffset();
    }

    protected void setOffset(int offset) {
        this.offset = Math.max(0, Math.min(offset, this.currentText.length()));
    }

    protected void setHighlight(int highlight) {
        this.highlight = Math.max(0, Math.min(highlight, this.currentText.length()));
    }

    @Override
    public void attachToSelector(BCSelector selector) {
        this.selector = selector;
    }

    public String getOffsetString() {
        return this.currentText.substring(Math.min(this.offset, this.currentText.length()));
    }

    public String getCurrentText() {
        return currentText;
    }

    public String getHighlighted() {
        if (highlightActive()) {
            int min = Math.min(this.highlight, this.pointer);
            int max = Math.max(this.highlight, this.pointer);
            return this.currentText.substring(min, max);
        }
        return null;
    }

    public int getBorderColor() {
        return borderColor;
    }

    protected int getNextWordIndex(int from, boolean backwards) {
        from = Mth.clamp(from, 0, this.currentText.length() - 1);
        char[] arr = this.currentText.toCharArray();
        int i;

        if (backwards) {
            for (i = from; i == from  || (i > 0 && !this.wordSeparator.test(arr[i - 1])); i--);
        } else {
            for (i = from; i == from || (i < arr.length && !this.wordSeparator.test(arr[i - 1])); i++);
        }
        return Mth.clamp(i, 0, arr.length);
    }

    @Override
    public BCSelector selector() {
        return this.selector;
    }

    protected Font font() {
        return font;
    }

    private int getMaxShowLength() {
        return Math.max(this.width - 6, 0);
    }

    private boolean highlightActive() {
        return this.highlight != this.pointer;
    }

    public boolean inbounds(double x, double y) {
        return x >= this.x && x < this.x + width && y >= this.y && y < this.y + this.height;
    }
}
