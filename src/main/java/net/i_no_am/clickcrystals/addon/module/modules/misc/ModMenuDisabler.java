package net.i_no_am.clickcrystals.addon.module.modules.misc;

import com.google.gson.*;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.FileValidationUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.i_no_am.clickcrystals.addon.client.data.Constants;
import net.i_no_am.clickcrystals.addon.module.AddonModule;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
    File MOD_MENU_CONFIG = FabricLoader.getInstance().getConfigDir().resolve("modmenu.json").toFile();


    @Override
    public void onEnable() {
        if (!FabricLoader.getInstance().isModLoaded("modmenu")) {
            toggle();
            if (mc.player != null) ChatUtils.sendWarningMessage("ModMenu is not installed, cannot hide ClickCrystals from mod menu.");
            return;
        }

        if (IDS == null) return;

        if (!FileValidationUtils.validate(MOD_MENU_CONFIG)) {
            JsonObject newConfig = new JsonObject();
            newConfig.add("hidden_mods", new JsonArray());
            FileValidationUtils.quickWrite(MOD_MENU_CONFIG, newConfig.toString());
        }

        try {
            JsonObject NEW_MOD_MENU_CONFIG;
            try (FileReader reader = new FileReader(MOD_MENU_CONFIG)) {
                NEW_MOD_MENU_CONFIG = JsonParser.parseReader(reader).getAsJsonObject();
            }

            JsonArray hiddenMods;
            if (NEW_MOD_MENU_CONFIG.has("hidden_mods") && NEW_MOD_MENU_CONFIG.get("hidden_mods").isJsonArray()) {
                hiddenMods = NEW_MOD_MENU_CONFIG.getAsJsonArray("hidden_mods");
            } else {
                hiddenMods = new JsonArray();
                NEW_MOD_MENU_CONFIG.add("hidden_mods", hiddenMods);
            }

            for (String modId : IDS) {
                addModToHiddenMods(hiddenMods, modId.trim());
            }

            FileValidationUtils.quickWrite(MOD_MENU_CONFIG, NEW_MOD_MENU_CONFIG.toString());

        } catch (IOException | JsonSyntaxException e) {
            system.printf("Error updating modmenu.json: %s", e.getMessage());
        }
    }

    private void addModToHiddenMods(JsonArray hiddenMods, String modId) {
        if (modId.isEmpty()) return;

        for (JsonElement element : hiddenMods) {
            if (element.getAsString().equals(modId)) {
                return;
            }
        }

        hiddenMods.add(modId);
    }
}