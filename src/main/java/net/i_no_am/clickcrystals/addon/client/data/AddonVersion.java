package net.i_no_am.clickcrystals.addon.client.data;

import io.github.itzispyder.clickcrystals.Global;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.i_no_am.clickcrystals.addon.utils.network.NetworkUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddonVersion implements Global {

    private final VersionStatus status;
    private boolean prompted = false;

    public AddonVersion() {
        String localVersion = fetchLocalVersion();
        String remoteVersion = fetchRemoteVersion();
        status = computeStatus(localVersion, remoteVersion);
    }

    public void notifyUpdate() {
        if (status == VersionStatus.UP_TO_DATE) return;
        if (!prompted && mc.screen == null && mc.player != null) {
            mc.setScreen(new ConfirmScreen(confirm -> {
                if (confirm) Util.getPlatform().openUri(URI.create(Constants.URL.DOWNLOAD));
                else mc.destroy();
            }, Component.nullToEmpty(ChatFormatting.DARK_RED + "You are using an outdated version of " + ChatFormatting.GREEN + "No one's Addon"), Component.nullToEmpty("Please download the latest version from " + ChatFormatting.DARK_PURPLE + "Discord"), Component.nullToEmpty("Download"), Component.nullToEmpty("Quit Game")));
            prompted = true;
        }
    }

    public enum VersionStatus {
        UP_TO_DATE, OUTDATED, UNKNOWN
    }

    private static VersionStatus computeStatus(String self, String remote) {
        try {
            double selfVer = Double.parseDouble(self);
            double remoteVer = Double.parseDouble(remote);

            if (selfVer >= remoteVer) return VersionStatus.UP_TO_DATE;
            else return VersionStatus.OUTDATED;
        } catch (Exception e) {
            system.logger.error("Failed to compare versions -> " + e.getMessage());
            return VersionStatus.UNKNOWN;
        }
    }

    private static String fetchLocalVersion() {
        try {
            ModContainer mod = FabricLoader.getInstance().getModContainer(Constants.VARS.MOD_ID).orElseThrow(() -> new IllegalStateException("Mod not found: " + Constants.VARS.MOD_ID));
            String friendlyVersion = mod.getMetadata().getVersion().getFriendlyString();
            Matcher matcher = Pattern.compile("(\\d+\\.\\d+)(?=-fabric|$)").matcher(friendlyVersion);
            if (matcher.find()) {
                return matcher.group(1);
            }
        } catch (Exception e) {
            system.logger.error("Failed to fetch self version -> " + e.getMessage());
        }
        return "0.0";
    }

    private static String fetchRemoteVersion() {
        try {
            var json = NetworkUtils.getJson(Constants.URL.API, "version");
            return json != null ? json.getAsString() : "0.0";
        } catch (Exception e) {
            system.logger.error("Failed to fetch remote version -> " + e.getMessage());
        }
        return "0.0";
    }
}