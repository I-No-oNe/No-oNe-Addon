package net.i_no_am.clickcrystals.addon.setting;

import java.util.List;

/**
 * A list of plain text entries, edited as rows right under the setting label.
 */
public class TextListSettingElement extends AbstractListSettingElement {

    public TextListSettingElement(TextListSetting setting, int x, int y) {
        super(setting, x, y);
    }

    @Override
    protected List<String> parseEntries(String value) {
        return TextListSetting.parse(value);
    }

    @Override
    protected String placeholder() {
        return "add entry";
    }
}
