package net.i_no_am.clickcrystals.addon.setting;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.misc.Shades;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * A list of items kept as text, drawn like a Meteor style picker: rows carry the
 * item icon and its name, the add row opens a searchable grid, and a mistyped
 * name stays visible as an honest unknown row instead of vanishing.
 */
public class ItemListSettingElement extends AbstractListSettingElement {

    private static final int ICON = 11, ICON_PAD = 2;
    private static final int CELL = 18, CELL_PAD = 2;
    private static final int PANEL_H = 128, PANEL_W_PAD = 10;
    private static final List<Item> ALL_ITEMS = new ArrayList<>();
    private static final Comparator<Item> BY_NAME = Comparator.comparing(
            ItemListSettingElement::displayName, String.CASE_INSENSITIVE_ORDER);

    private boolean pickerOpen;
    private String filter = "";
    private int scroll;
    private int panelX, panelY, panelW;

    public ItemListSettingElement(ItemListSetting setting, int x, int y) {
        super(setting, x, y);
    }

    // ------------- data -------------

    @Override
    protected List<String> parseEntries(String value) {
        return TextListSetting.parse(value);
    }

    @Override
    protected String placeholder() {
        return "add item";
    }

    private static String displayName(Item item) {
        return item.getName(item.getDefaultInstance()).getString();
    }

    private List<Item> filteredItems() {
        if (ALL_ITEMS.isEmpty()) {
            for (Item item : BuiltInRegistries.ITEM) {
                try {
                    if (item.getDefaultInstance() != null && !item.getDefaultInstance().isEmpty()) {
                        ALL_ITEMS.add(item);
                    }
                }
                catch (Exception ignored) {
                    // a handful of items refuse a default stack; they are not usable here anyway
                }
            }
            ALL_ITEMS.sort(BY_NAME);
        }
        String needle = ItemListSetting.normalize(filter);
        if (needle.isEmpty()) {
            return ALL_ITEMS;
        }
        List<Item> matches = new ArrayList<>();
        for (Item item : ALL_ITEMS) {
            if (ItemListSetting.idOf(item).contains(needle)
                    || displayName(item).toLowerCase().replace(' ', '_').contains(needle)) {
                matches.add(item);
            }
        }
        return matches;
    }

    // ------------- rows with icons -------------

    @Override
    protected int renderRowIcon(GuiGraphicsExtractor context, String entry, int iconX, int rowTop) {
        if (entry == null || entry.isBlank()) {
            return 0;
        }
        Item item = ItemListSetting.itemOf(entry);
        if (item == null) {
            RenderUtils.fillRoundRect(context, iconX, rowTop + ICON_PAD + 1, ICON, ICON, 2, 0xFF4D1111);
            RenderUtils.drawText(context, "§c?", iconX + 4, rowTop + ICON_PAD + 2, 0.6F, false);
        }
        else {
            RenderUtils.drawItem(context, item.getDefaultInstance(), iconX, rowTop + ICON_PAD + 1, ICON);
        }
        return ICON + 3;
    }

    // ------------- picker panel -------------

    private void openPicker() {
        pickerOpen = true;
        filter = "";
        scroll = 0;
    }

    private void closePicker() {
        pickerOpen = false;
        filter = "";
        scroll = 0;
    }

    private boolean hoversPanel(int mouseX, int mouseY) {
        return pickerOpen && mouseX >= panelX && mouseX <= panelX + panelW
                && mouseY >= panelY && mouseY <= panelY + PANEL_H;
    }

    @Override
    protected void onAddRowClick(GuiScreen screen) {
        if (pickerOpen) {
            closePicker();
        }
        else {
            openPicker();
        }
    }

    @Override
    protected int renderExtra(GuiGraphicsExtractor context, int mouseX, int mouseY, int cursor) {
        if (!pickerOpen) {
            return cursor;
        }

        panelW = width - PANEL_W_PAD;
        panelX = x + INSET;
        panelY = cursor;
        RenderUtils.fillRoundRect(context, panelX, panelY, panelW, PANEL_H, 4, Shades.DARK_GRAY);
        RenderUtils.drawHorLine(context, panelX, panelY + 14, panelW, Shades.GRAY);

        String filterLine = filter.isEmpty() ? "§7search items..." : filter + "§8§l︳";
        RenderUtils.drawText(context, filterLine, panelX + 5, panelY + 4, 0.7F, false);

        List<Item> items = filteredItems();
        int cols = Math.max(1, (panelW - 8) / CELL);
        int perPage = cols * Math.max(1, (PANEL_H - 20) / CELL);
        int maxScroll = Math.max(0, (items.size() + perPage - 1) / perPage - 1);
        scroll = Math.min(scroll, maxScroll);

        int first = scroll * perPage;
        for (int i = 0; i < perPage && first + i < items.size(); i++) {
            int row = i / cols, col = i % cols;
            int cellX = panelX + 5 + col * CELL, cellY = panelY + 18 + row * CELL;
            int side = CELL - CELL_PAD;
            if (mouseX >= cellX && mouseX <= cellX + side && mouseY >= cellY && mouseY <= cellY + side) {
                RenderUtils.fillRoundRect(context, cellX, cellY, side, side, 2, Shades.GRAY);
            }
            RenderUtils.drawItem(context, items.get(first + i).getDefaultInstance(),
                    cellX + (side - 14) / 2, cellY + (side - 14) / 2, 14);
        }

        if (items.isEmpty()) {
            RenderUtils.drawText(context, "§cno items matched", panelX + 5, panelY + 20, 0.7F, false);
        }
        if (maxScroll > 0) {
            RenderUtils.drawText(context, "§7" + (scroll + 1) + " / " + (maxScroll + 1),
                    panelX + panelW - 34, panelY + 4, 0.6F, false);
        }
        return cursor + PANEL_H + 4;
    }

    @Override
    protected boolean onExtraClick(double mouseX, double mouseY, int button) {
        if (pickerOpen && hoversPanel((int) mouseX, (int) mouseY)) {
            int cols = Math.max(1, (panelW - 8) / CELL);
            List<Item> items = filteredItems();
            int perPage = cols * Math.max(1, (PANEL_H - 20) / CELL);
            int first = scroll * perPage;

            for (int i = 0; i < perPage && first + i < items.size(); i++) {
                int row = i / cols, col = i % cols;
                int side = CELL - CELL_PAD;
                int cellX = panelX + 5 + col * CELL, cellY = panelY + 18 + row * CELL;
                if (mouseX >= cellX && mouseX <= cellX + side && mouseY >= cellY && mouseY <= cellY + side) {
                    getRows().add(ItemListSetting.idOf(items.get(first + i)));
                    save();
                    break;
                }
            }
            return true;
        }
        if (pickerOpen) {
            closePicker();
            return true;
        }
        return false;
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, int amount) {
        if (hoversPanel((int) mouseX, (int) mouseY)) {
            scroll = Math.max(0, scroll - amount);
            return;
        }
        super.mouseScrolled(mouseX, mouseY, amount);
    }

    // ------------- typing goes to the filter while the picker shows -------------

    @Override
    public boolean onKey(int key, int scan) {
        if (pickerOpen) {
            if (key == InputConstants.KEY_ESCAPE || key == InputConstants.KEY_BACKSPACE && filter.isEmpty()) {
                closePicker();
                return true;
            }
            if (key == InputConstants.KEY_RETURN && !filteredItems().isEmpty()) {
                getRows().add(ItemListSetting.idOf(filteredItems().get(0)));
                save();
                return true;
            }
            return typeableOnKey(key, scan);
        }
        return super.onKey(key, scan);
    }

    @Override
    protected boolean acceptsTyping() {
        return super.acceptsTyping() || pickerOpen;
    }

    @Override
    public void onInput(Function<String, String> factory) {
        if (pickerOpen) {
            filter = factory.apply(filter);
            scroll = 0;
            return;
        }
        super.onInput(factory);
    }

    /** Losing the selection while browsing closes the panel, like any picker. */
    @Override
    public void postRender(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        super.postRender(context, mouseX, mouseY);
        if (pickerOpen && mc.gui.screen() instanceof GuiScreen screen && screen.selected != this) {
            closePicker();
        }
    }
}
