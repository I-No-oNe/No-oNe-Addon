package net.i_no_am.clickcrystals.addon.setting;

import io.github.itzispyder.clickcrystals.modules.settings.SettingBuilder;
import io.github.itzispyder.clickcrystals.modules.settings.StringSetting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * A list of items owned by the addon, carried as item names in the joined string
 * value, so ClickCrystals' own config keeps saving and loading it like a text
 * setting. Entries accept plain names ("diamond") or namespaced ones
 * ("minecraft:diamond") and match both ways.
 */
public class ItemListSetting extends StringSetting {

    public ItemListSetting(String name, String description, String def, String val) {
        super(name, description, def, val);
    }

    /** Raw entries as typed; unmodifiable. */
    public List<String> getRawItems() {
        return TextListSetting.parse(getVal());
    }

    /** The hotbar or inventories rarely care about case or namespaces, so accept either. */
    public boolean matches(ItemStack stack) {
        return stack != null && !stack.isEmpty() && matches(stack.getItem());
    }

    public boolean matches(Item item) {
        String id = normalize(idOf(item));
        for (String entry : getRawItems()) {
            if (normalize(entry).equals(id)) return true;
        }
        return false;
    }

    /** Words with spaces match underscored ids, so "firework rocket" finds the rocket. */
    public static String normalize(String entry) {
        return entry.trim().toLowerCase(Locale.ROOT).replace(' ', '_');
    }

    public static String idOf(Item item) {
        Identifier id = BuiltInRegistries.ITEM.getKey(item);
        return id != null ? id.getPath() : "unknown";
    }

    /** A registered item for a typed entry, or null when unknown. */
    public static Item itemOf(String entry) {
        Identifier id = idOfRegistry(entry);
        if (id == null || !BuiltInRegistries.ITEM.containsKey(id)) {
            return null;
        }
        return BuiltInRegistries.ITEM.getValue(id);
    }

    /** Registry id for an entry: plain names resolve under minecraft, namespaced pass through. */
    public static Identifier idOfRegistry(String entry) {
        String normalized = normalize(entry);
        return Identifier.tryParse(normalized.contains(":") ? normalized : "minecraft:" + normalized);
    }

    @Override
    public ItemListSettingElement toGuiElement(int x, int y) {
        return new ItemListSettingElement(this, x, y);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends SettingBuilder<String, Builder, ItemListSetting> {

        public Builder def(Item... items) {
            String[] ids = new String[items.length];
            for (int i = 0; i < items.length; i++) ids[i] = idOf(items[i]);
            return def(String.join(TextListSetting.SEPARATOR, ids));
        }

        @Override
        public ItemListSetting buildSetting() {
            return new ItemListSetting(name, description, def, getOrDef(val, def));
        }
    }
}
