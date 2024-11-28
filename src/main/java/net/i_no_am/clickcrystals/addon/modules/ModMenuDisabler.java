package net.i_no_am.clickcrystals.addon.modules;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import net.fabricmc.loader.api.FabricLoader;
import net.i_no_am.clickcrystals.addon.client.Manager;
import net.i_no_am.clickcrystals.addon.modules.data.AddonModule;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class ModMenuDisabler extends AddonModule {

    public ModMenuDisabler() {
        super("mod-menu-disabler", "disable clickcrystals from showing on mod menu, re-launch game after enabling the module");
    }

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<String> modIdNames = scGeneral.add(createStringSetting()
            .name("mods-id's")
            .description("Comma-separated list of mod IDs that you want to hide.")
            .def(Manager.MOD_ID + "," + modId)
            .build()
    );

    @Override
    public void onEnable() {
        String modIdsString = modIdNames.getVal();
        if (modIdsString.isEmpty()) return;

        String[] modIds = modIdsString.split(",");
        try {
            Path configDir = FabricLoader.getInstance().getConfigDir();
            File modMenuConfigFile = configDir.resolve("modmenu.json").toFile();

            if (!modMenuConfigFile.exists()) {
                system.printf("modmenu.json does not exist.");
                return;
            }

            JsonObject modMenuConfig;
            try (FileReader reader = new FileReader(modMenuConfigFile)) {
                modMenuConfig = JsonParser.parseReader(reader).getAsJsonObject();
            }

            JsonArray hiddenMods;
            if (modMenuConfig.has("hidden_mods") && modMenuConfig.get("hidden_mods").isJsonArray()) {
                hiddenMods = modMenuConfig.getAsJsonArray("hidden_mods");
            } else {
                hiddenMods = new JsonArray();
                modMenuConfig.add("hidden_mods", hiddenMods);
            }

            for (String modId : modIds) {
                addModToHiddenMods(hiddenMods, modId.trim());
            }

            try (FileWriter writer = new FileWriter(modMenuConfigFile)) {
                writer.write(modMenuConfig.toString());
                writer.flush();
            }
        } catch (IOException | JsonSyntaxException ignore) {
        }
    }

    private void addModToHiddenMods(JsonArray hiddenMods, String modId) {
        if (modId.isEmpty()) return;

        boolean alreadyHidden = false;
        for (JsonElement element : hiddenMods) {
            if (element.getAsString().equals(modId)) {
                alreadyHidden = true;
                break;
            }
        }
        if (!alreadyHidden) {
            hiddenMods.add(modId);
        }
    }
}
