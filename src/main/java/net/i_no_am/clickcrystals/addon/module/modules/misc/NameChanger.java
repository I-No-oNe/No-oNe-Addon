package net.i_no_am.clickcrystals.addon.module.modules.misc;

import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import net.i_no_am.clickcrystals.addon.module.AddonModule;

public class NameChanger extends AddonModule {
    public NameChanger() {
        super("name-changer", "Replaces your name with another one everywhere the client draws it");
    }

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<String> fakeName = scGeneral.add(createStringSetting()
            .name("fake-name")
            .description("Name shown in place of your own.")
            .def("Steve1")
            .build()
    );
}
