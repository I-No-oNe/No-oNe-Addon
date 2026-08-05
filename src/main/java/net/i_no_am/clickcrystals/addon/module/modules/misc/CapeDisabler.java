package net.i_no_am.clickcrystals.addon.module.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import net.i_no_am.clickcrystals.addon.listener.events.cc.PlayerJoinEvent;
import net.i_no_am.clickcrystals.addon.module.AddonListenerModule;

public class CapeDisabler extends AddonListenerModule {
    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> disableStaffNotifications = scGeneral.add(createBoolSetting()
            .name("disable-staff-notifications")
            .description("Skip the message announcing that staff joined the world.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> disableClickCrystalCapes = scGeneral.add(createBoolSetting()
            .name("disable-capes")
            .description("Hide ClickCrystals capes so vanilla ones show through.")
            .def(true)
            .build()
    );

    public CapeDisabler() {
        super("cape-disabler", "Shows vanilla capes instead of ClickCrystals ones, and drops the staff greetings");
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {
        if (isEnabled() && disableStaffNotifications.getVal())
            e.cancel();
    }
}
