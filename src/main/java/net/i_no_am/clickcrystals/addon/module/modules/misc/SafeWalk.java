package net.i_no_am.clickcrystals.addon.module.modules.misc;

import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import net.i_no_am.clickcrystals.addon.module.AddonModule;
import net.i_no_am.clickcrystals.addon.setting.ItemListSetting;

public class SafeWalk extends AddonModule {
    public SafeWalk() {
        super("safe-walk", "Holds you back at block edges the way sneaking does");
    }

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> itemCheck = scGeneral.add(createBoolSetting()
            .name("only-while-holding")
            .description("Only hold you back while a chosen item is in hand.")
            .def(false)
            .build()
    );
    public final ItemListSetting itemNames = scGeneral.add(ItemListSetting.builder()
            .name("required-items")
            .description("Items that switch the module on while held.")
            .def("")
            .visibleWhen(itemCheck::getVal)
            .build()
    );

    public boolean isOn() {
        return isEnabled() && (!itemCheck.getVal() || itemNames.matches(HotbarUtils.getHand()));
    }
}
