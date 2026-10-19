package net.i_no_am.clickcrystals.addon.setting;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.elements.browsingmode.module.StringSettingElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.Typeable;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.gui.misc.animators.Animations;
import io.github.itzispyder.clickcrystals.gui.misc.animators.Animator;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.GuiBorders;
import io.github.itzispyder.clickcrystals.modules.settings.StringSetting;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Rows of text you edit in place, owned by the addon so ClickCrystals needs no
 * list settings of its own. Rows sit under the label, the last row is a free
 * slot for writing more, and the value stays a plain string so ClickCrystals'
 * config keeps saving it like any text setting.
 */
public abstract class AbstractListSettingElement extends StringSettingElement implements Typeable {

    public static final int ROW_HEIGHT = 16, ROW_GAP = 2, INSET = 5, BOTTOM_PAD = 8;
    private static final int DELETE_ZONE = 14;
    private static final long VISIBILITY_ANIM_MS = 250;

    private Animator visibilityAnimator;
    private boolean lastVisible;

    private final List<String> rows = new ArrayList<>();
    private int editing = -1;
    private int labelHeight;

    public AbstractListSettingElement(StringSetting setting, int x, int y) {
        super(setting, x, y);
        this.lastVisible = getSetting().isVisible();
    }

    // ------------- row state -------------

    protected boolean isEditing() {
        return editing >= 0 && editing < rows.size();
    }

    protected List<String> getRows() {
        return rows;
    }

    /** Pulls the current value into the rows, called whenever no row is open for typing. */
    protected void syncRows() {
        rows.clear();
        rows.addAll(parseEntries(getSetting().getVal()));
    }

    /** Writes the rows back as a joined string, dropping blanks and whitespace. */
    protected void save() {
        List<String> cleaned = new ArrayList<>();
        for (String row : rows) {
            String trimmed = row == null ? "" : row.trim();
            if (!trimmed.isEmpty()) cleaned.add(trimmed);
        }
        getSetting().setVal(String.join(",", cleaned));
    }

    /** Commits the open row and appends an empty one below, so typing walks down. */
    protected void enterRow() {
        if (!isEditing()) return;
        rows.set(editing, rows.get(editing).trim());
        getSetting().setVal(TextListSetting.join(rows));
        rows.add("");
        editing++;
    }

    protected void tabRow() {
        if (!isEditing()) return;
        rows.set(editing, rows.get(editing).trim());
        getSetting().setVal(TextListSetting.join(rows));
        editing = Math.min(editing + 1, rows.size() - 1);
    }

    protected void deleteRow(int index) {
        if (isEditing()) rows.set(editing, rows.get(editing).trim());
        rows.remove(index);
        boolean wasFocused = editing == index;
        save();
        if (!wasFocused) {
            if (editing > index) editing--;
            return;
        }
        if (rows.isEmpty()) {
            rows.add("");
            editing = 0;
        }
        else {
            editing = Math.min(index, rows.size() - 1);
        }
    }

    /** Commits the currently edited row and closes the caret. */
    protected void commit() {
        if (isEditing()) {
            save();
        }
        editing = -1;
    }

    // ------------- geometry -------------

    private int rowTop(int index) {
        int which = index < 0 ? rows.size() : index;
        return y + labelHeight + which * (ROW_HEIGHT + ROW_GAP);
    }

    /** True while the pointer sits over one of a row's side zones. */
    private boolean hoversDelete(int mouseX, int mouseY, int index) {
        int deleteX = x + width - INSET - DELETE_ZONE;
        int top = rowTop(index);
        return mouseX >= deleteX && mouseY >= top && mouseX <= deleteX + DELETE_ZONE && mouseY <= top + ROW_HEIGHT;
    }

    private boolean hoversRow(int mouseX, int mouseY, int index) {
        int top = rowTop(index);
        return mouseX >= x + INSET && mouseX <= x + width - INSET && mouseY >= top && mouseY <= top + ROW_HEIGHT;
    }

    // ------------- rendering -------------

    @Override
    public void onRender(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        renderSettingDetails(context);
        labelHeight = this.height;
        if (!isEditing()) syncRows();

        int cursor = y + labelHeight;
        for (int i = 0; i < rows.size(); i++) {
            renderRow(context, mouseX, mouseY, rowTop(i), i);
            cursor += ROW_HEIGHT + ROW_GAP;
        }
        renderAddRow(context, mouseX, mouseY, rowTop(-1));
        cursor += ROW_HEIGHT + ROW_GAP;

        cursor = renderExtra(context, mouseX, mouseY, cursor);
        height = cursor + BOTTOM_PAD - y;
    }

    private void renderRow(GuiGraphicsExtractor context, int mouseX, int mouseY, int top, int index) {
        String entry = rows.get(index);
        boolean focused = isEditing() && editing == index;
        boolean hovering = hoversRow(mouseX, mouseY, index);
        boolean hoveringDelete = !focused && hoversDelete(mouseX, mouseY, index);

        RenderUtils.fillRoundRect(context, x + INSET, top, width - INSET * 2, ROW_HEIGHT, 3,
                focused || hovering ? Shades.LIGHT_GRAY : Shades.GRAY);
        if (focused) {
            RenderUtils.drawHorLine(context, x + INSET, top + ROW_HEIGHT, width - INSET * 2, Shades.DARK_GRAY);
        }

        int textX = x + INSET + 4 + renderRowIcon(context, entry, x + INSET + 2, top);
        int maxTextWidth = width - INSET * 2 - DELETE_ZONE - 12 - (textX - (x + INSET));
        String text = focused && entry.isEmpty() ? "" : entry;
        while (!text.isEmpty() && mc.font.width(text) * 0.7F > maxTextWidth) {
            text = text.substring(1);
        }
        String caret = focused ? "§8§l︳" : "";
        RenderUtils.drawText(context, text + caret, textX, top + 4, 0.7F, false);

        if (!focused && hovering) {
            int inTheCorner = DELETE_ZONE - 6;
            int deleteX = x + width - INSET - inTheCorner;
            RenderUtils.drawText(context, hoveringDelete ? "§c×" : "§8×", deleteX, top + 4, 0.9F, false);
        }
    }

    private void renderAddRow(GuiGraphicsExtractor context, int mouseX, int mouseY, int top) {
        boolean hovering = hoversRow(mouseX, mouseY, -1);

        RenderUtils.fillRoundRect(context, x + INSET, top, width - INSET * 2, ROW_HEIGHT, 3,
                hovering ? Shades.LIGHT_GRAY : Shades.GRAY);
        RenderUtils.drawText(context, "§7✚ " + placeholder(), x + INSET + 4, top + 4, 0.7F, false);
    }

    /** Per-entry icon space ahead of the row text; return 0 to skip. */
    protected int renderRowIcon(GuiGraphicsExtractor context, String entry, int iconX, int rowTop) {
        return 0;
    }

    /** Draws an addon-owned block below the rows; returns where the next pixel row starts. */
    protected int renderExtra(GuiGraphicsExtractor context, int mouseX, int mouseY, int cursor) {
        return cursor;
    }

    protected abstract String placeholder();

    /** The saved victim value its rows read back from. */
    protected abstract List<String> parseEntries(String value);

    // ------------- clicks -------------

    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        return rendering && getSetting().isVisible()
                && mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (mc.gui.screen() instanceof GuiScreen screen && getSetting().isVisible() && isHovered((int) mouseX, (int) mouseY)) {

            if (onExtraClick(mouseX, mouseY, button)) {
                screen.selected = this;
                return;
            }

            if (hoversRow((int) mouseX, (int) mouseY, -1)) {
                commit();
                onAddRowClick(screen);
                screen.selected = this;
                return;
            }

            for (int i = rows.size() - 1; i >= 0; i--) {
                if (!hoversRow((int) mouseX, (int) mouseY, i)) continue;
                if (hoversDelete((int) mouseX, (int) mouseY, i)) {
                    deleteRow(i);
                }
                else {
                    commit();
                    editing = i;
                }
                screen.selected = this;
                return;
            }

            commit();
        }
        super.mouseClicked(mouseX, mouseY, button);
    }

    /** What a click on the free slot row does. */
    protected void onAddRowClick(GuiScreen screen) {
        rows.add("");
        editing = rows.size() - 1;
    }

    /**
     * Lets a picker panel claim clicks before the rows react. Return true when the
     * click counts as handled, even if only to close the panel.
     */
    protected boolean onExtraClick(double mouseX, double mouseY, int button) {
        return false;
    }

    // ------------- keyboard -------------

    @Override
    public boolean onKey(int key, int scan) {
        if (!isEditing()) {
            return false;
        }
        if (key == InputConstants.KEY_RETURN) {
            enterRow();
            return true;
        }
        if (key == InputConstants.KEY_TAB) {
            tabRow();
            return true;
        }
        if (key == InputConstants.KEY_BACKSPACE && rows.get(editing).isEmpty()) {
            deleteRow(editing);
            return true;
        }
        if (key == InputConstants.KEY_ESCAPE) {
            commit();
        }
        return typeableOnKey(key, scan);
    }

    @Override
    public void onChar(char chr) {
        if (acceptsTyping() && !Character.isISOControl(chr)) {
            onInput(input -> input.concat(String.valueOf(chr)));
        }
    }

    @Override
    public void onInput(Function<String, String> factory) {
        if (isEditing()) {
            rows.set(editing, factory.apply(rows.get(editing)));
            save();
        }
    }

    /** Whether typed characters land anywhere right now. */
    protected boolean acceptsTyping() {
        return isEditing();
    }

    /** The shared typing fallback: escape, backspace trimming, ctrl + paste. */
    protected boolean typeableOnKey(int key, int scan) {
        if (!(mc.gui.screen() instanceof GuiScreen screen)) {
            return false;
        }
        if (key == InputConstants.KEY_ESCAPE) {
            screen.selected = null;
            return true;
        }
        if (key == InputConstants.KEY_BACKSPACE) {
            onInput(input -> input.isEmpty()
                    ? input
                    : input.substring(0, input.length() - 1));
            return true;
        }
        if (key == InputConstants.KEY_V && screen.ctrlKeyPressed) {
            onInput(input -> input.concat(mc.keyboardHandler.getClipboard()));
            return true;
        }
        return false;
    }

    /** Clicking another element hands the caret away: commit before that sticks. */
    @Override
    public void postRender(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        if (!isEditing()) return;
        if (!(mc.gui.screen() instanceof GuiScreen screen) || screen.selected != this) {
            commit();
        }
    }

    // ------------- visibility, matching the section's own fade on our full height -------------

    @Override
    public int getLayoutHeight() {
        return (int) (height * visibilityFactor());
    }

    private double visibilityFactor() {
        boolean visible = getSetting().isVisible();
        if (visible != lastVisible) {
            lastVisible = visible;
            visibilityAnimator = new Animator(VISIBILITY_ANIM_MS, Animations.FADE_IN_AND_OUT);
            visibilityAnimator.setReversed(!visible);
            visibilityAnimator.reset();
        }
        if (visibilityAnimator == null) {
            return visible ? 1 : 0;
        }
        if (visibilityAnimator.isFinished()) {
            visibilityAnimator = null;
            return visible ? 1 : 0;
        }
        return MathUtils.clamp(visibilityAnimator.getAnimation(), 0.0, 1.0);
    }

    @Override
    public void render(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        double factor = visibilityFactor();
        if (factor <= 0.01) {
            setRendering(false);
            return;
        }
        setRendering(true);

        // the base element clips to a fixed 40px slot while fading, so tall settings
        // pop instead of growing; clip around our real height instead
        if (factor < 0.99) {
            int clip = Math.max(1, (int) (height * factor));
            context.enableScissor(x, y, x + width, y + clip);
        }
        renderListContent(context, mouseX, mouseY);
        if (factor < 0.99) {
            context.disableScissor();
        }
    }

    private void renderListContent(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        boolean canRender = canRender();
        if (canRender) {
            onRender(context, mouseX, mouseY);
        }
        for (GuiElement child : getChildren()) {
            child.render(context, mouseX, mouseY);
        }
        Module guiBorders = Module.get(GuiBorders.class);
        if (guiBorders.isEnabled()) {
            RenderUtils.drawRect(context, x, y, width, height, 0xFFFFFFFF);
        }
        if (canRender) {
            postRender(context, mouseX, mouseY);
        }
    }
}
