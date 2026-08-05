package net.i_no_am.clickcrystals.addon.module.modules.misc;

import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import net.i_no_am.clickcrystals.addon.module.AddonModule;

public class GUIFeaturesDisabler extends AddonModule {
    public GUIFeaturesDisabler() {
        super("gui-features-disabler", "Strips the popups ClickCrystals shows over its own menu");
    }
    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> discordDisable = scGeneral.add(createBoolSetting()
            .name("disable-discord-screen")
            .description("Skip the Discord invite shown when the menu opens.")
            .def(true)
            .build()
    );

    public final ModuleSetting<Boolean> newsDisable = scGeneral.add(createBoolSetting()
            .name("disable-news-ping")
            .description("Hide the unread announcements badge on the menu.")
            .def(true)
            .build()
    );
}