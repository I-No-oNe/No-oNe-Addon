package net.i_no_am.clickcrystals.addon.module.modules.misc;

import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import net.i_no_am.clickcrystals.addon.module.AddonModule;

import java.util.Arrays;

public class SafeWalk extends AddonModule {
    public SafeWalk() {
        super("safe-walk", "Automatically sneaks at the edge of blocks");
    }

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> itemCheck = scGeneral.add(createBoolSetting()
            .name("check-item-name")
            .description("If holding the requited item the module will work.")
            .def(false)
            .build()
    );
    public final ModuleSetting<String> itemNames = scGeneral.add(createStringSetting()
            .name("items-to-check")
            .description("Write down here the names of the items")
            .def("")
            .build()
    );

    public boolean isOn() {
        return isEnabled() && (!itemCheck.getVal() || Arrays.stream(itemNames.getVal().toLowerCase().split(",")).map(String::trim).anyMatch(HotbarUtils::nameContains));
    }
}
