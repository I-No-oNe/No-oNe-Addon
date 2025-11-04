package net.i_no_am.clickcrystals.addon.utils;

import io.github.itzispyder.clickcrystals.Global;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.i_no_am.clickcrystals.addon.utils.OsUtils.SYSTEM.*;

public class OsUtils implements Global {

    private static final String OS = System.getProperty("os.name").toLowerCase();

    public enum SYSTEM {
        WINDOW, LINUX, MAC
    }

    public static @NotNull SYSTEM getOs() {
        if (OS.contains("win")) return WINDOW;
        if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix")) return LINUX;
        if (OS.contains("mac") || OS.contains("darwin")) return MAC;
        throw new UnsupportedOperationException("Unsupported operating system: " + OS);
    }

    public static void copy(String text) {
        StringSelection selection = new StringSelection(text);
        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(selection, null);
    }

    public static String getHWID() {
        try {
            return switch (getOs()) {
                case WINDOW -> getWindowsHWID();
                case MAC -> getMacHWID();
                case LINUX -> getLinuxHWID();
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getWindowsHWID() throws Exception {
        String[] commands = {
                "wmic csproduct get uuid",            // Most reliable
                "wmic baseboard get serialnumber",    // Fallback
                "wmic bios get serialnumber"          // Fallback
        };

        for (String command : commands) {
            String result = runCommandAndMatch(command, "\\b[0-9a-fA-F]{8}(?:-[0-9a-fA-F]{4}){3}-[0-9a-fA-F]{12}\\b");
            if (result != null) return result;
        }

        return "UNKNOWN-WINDOWS-HWID";
    }

    private static String getMacHWID() throws Exception {
        String command = "ioreg -rd1 -c IOPlatformExpertDevice";
        String pattern = "\"IOPlatformUUID\"\\s=\\s\"(.*?)\"";

        String result = runCommandAndMatch(new String[]{"/bin/sh", "-c", command}, pattern);
        return result != null ? result : "UNKNOWN-MAC-HWID";
    }

    private static String getLinuxHWID() throws Exception {
        String[] commands = {
                "cat /etc/machine-id",                     // Most common
                "cat /var/lib/dbus/machine-id",            // Sometimes available
                "hostnamectl | grep 'Machine ID'"          // More verbose, parse needed
        };

        for (String cmd : commands) {
            String result = runCommandAndMatch(cmd, "[a-f0-9\\-]{16,}");
            if (result != null) return result;
        }

        return "UNKNOWN-LINUX-HWID";
    }

    private static String runCommandAndMatch(String command, String regex) throws Exception {
        String[] commandArray;
        // Use cmd.exe on Windows:
        if (getOs() == WINDOW) commandArray = new String[]{"cmd.exe", "/c", command};
        else commandArray = new String[]{"/bin/sh", "-c", command};
        return runCommandAndMatch(commandArray, regex);
    }


    private static String runCommandAndMatch(String[] command, String regex) throws Exception {
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true);
        Process process = builder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            Pattern pattern = Pattern.compile(regex);
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line.trim());
                if (matcher.find()) {
                    if (matcher.groupCount() >= 1) {
                        String g1 = matcher.group(1);
                        if (g1 != null && !g1.isEmpty()) return g1;
                    }
                    return matcher.group(0);
                }
            }
        }
        return null;
    }
}