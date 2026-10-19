package net.i_no_am.clickcrystals.addon.setting;

import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingBuilder;
import io.github.itzispyder.clickcrystals.modules.settings.StringSetting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A list of plain text entries owned by the addon, so no ClickCrystals side changes
 * are needed to carry it. Stored as the joined string value so ClickCrystals' own
 * config keeps saving and loading it like any text setting.
 */
public class TextListSetting extends StringSetting {

    public static final String SEPARATOR = ",";

    public TextListSetting(String name, String description, String def, String val) {
        super(name, description, def, val);
    }

    /** Entries as typed, unmodifiable: no blanks, no separators. */
    public List<String> getEntries() {
        return parse(getVal());
    }

    /** Entries without blanks, each trimmed; used by keyword style lookups. */
    public static List<String> parse(String val) {
        if (val == null || val.isBlank()) return List.of();
        List<String> entries = new ArrayList<>();
        for (String entry : val.split(SEPARATOR)) {
            String trimmed = entry.trim();
            if (!trimmed.isEmpty()) entries.add(trimmed);
        }
        return Collections.unmodifiableList(entries);
    }

    public static String join(List<String> entries) {
        List<String> trimmed = new ArrayList<>();
        for (String entry : entries) {
            String string = entry == null ? "" : entry.trim();
            if (!string.isEmpty()) trimmed.add(string);
        }
        return String.join(SEPARATOR, trimmed);
    }

    @Override
    public TextListSettingElement toGuiElement(int x, int y) {
        return new TextListSettingElement(this, x, y);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends SettingBuilder<String, Builder, TextListSetting> {

        public Builder def(String... entries) {
            return def(join(Arrays.asList(entries)));
        }

        @Override
        public TextListSetting buildSetting() {
            return new TextListSetting(name, description, def, getOrDef(val, def));
        }
    }
}
