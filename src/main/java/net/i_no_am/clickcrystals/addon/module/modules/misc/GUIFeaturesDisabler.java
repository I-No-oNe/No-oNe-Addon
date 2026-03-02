package net.i_no_am.clickcrystals.addon.module.modules.misc;

import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import net.i_no_am.clickcrystals.addon.module.AddonModule;

public class GUIFeaturesDisabler extends AddonModule {
    public GUIFeaturesDisabler() {
        super("GUI-features-disabler", "Cancels the annoying things inside the ClickCrystals GUI");
    }
    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> discordDisable = scGeneral.add(createBoolSetting()
            .name("discord-screen-disable")
            .description("Disable the join discord screen when opening the ClickCrystals GUI.")
            .def(true)
            .build()
    );

    public final ModuleSetting<Boolean> newsDisable = scGeneral.add(createBoolSetting()
            .name("news-ping-disable")
            .description("Disable the news ping that appears when opening the ClickCrystals GUI.")
            .def(true)
            .build()
    );
}