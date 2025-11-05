package net.i_no_am.clickcrystals.addon.module.modules.misc;

import com.terraformersmc.modmenu.ModMenu;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import net.i_no_am.clickcrystals.addon.client.data.Constants;
import net.i_no_am.clickcrystals.addon.module.AddonModule;

public class ModMenuDisabler extends AddonModule {

    public ModMenuDisabler() {
        super("mod-menu-disabler", "Hides ClickCrystals from mod menu, REQUIRES RESTART");
    }

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<String> modIdNames = scGeneral.add(createStringSetting()
            .name("mods-id's")
            .description("Comma-separated list of mod IDs that you want to hide.")
            .def(Constants.VARS.MOD_ID + "," + modId)
            .build()
    );

    String[] IDS = modIdNames.getVal().split(",");

    public void hideMods() {
        ModMenu.MODS.forEach((id, mod) -> {
            if (isHidden(id)) ModMenu.MODS.remove(id);
        });

        ModMenu.ROOT_MODS.forEach((id, mod) -> {
            if (isHidden(id)) ModMenu.MODS.remove(id);
        });

        ModMenu.PARENT_MAP.clear();
        ModMenu.clearModCountCache();
    }

    public static boolean isHidden(String modId) {
        var mod = get(ModMenuDisabler.class);
        for (String id : mod.IDS) {
            if (id.trim().equalsIgnoreCase(modId)) {
                return true;
            }
        }
        return false;
    }

    public String numberOfHiddenMods() {
        return String.valueOf(IDS.length);
    }
}