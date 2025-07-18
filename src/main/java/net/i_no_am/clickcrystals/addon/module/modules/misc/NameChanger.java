package net.i_no_am.clickcrystals.addon.module.modules.misc;

import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import net.i_no_am.clickcrystals.addon.module.AddonModule;

public class NameChanger extends AddonModule {
    public NameChanger() {
        super("name-changer", "Changes the displayed name of the player (client-side only)");
    }

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<String> fakeName = scGeneral.add(createStringSetting()
            .name("player-name")
            .description("A name that your real name will be replaced with")
            .def("Steve1")
            .build()
    );
}
