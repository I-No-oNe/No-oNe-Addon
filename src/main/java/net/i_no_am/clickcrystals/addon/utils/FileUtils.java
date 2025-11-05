package net.i_no_am.clickcrystals.addon.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.util.FileValidationUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class FileUtils implements Global {
    // Ik this code isn't the best :(
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Type MAP_TYPE = new TypeToken<Map<String, Object>>() {
    }.getType();

    private static File getConfigFile() {
        return new File(".clickcrystals/addon/config.json");
    }

    private static Map<String, Object> readConfig() {
        File file = getConfigFile();
        if (!FileValidationUtils.validate(file)) return new HashMap<>();

        try (Reader reader = new FileReader(file)) {
            Map<String, Object> map = gson.fromJson(reader, MAP_TYPE);
            return map != null ? map : new HashMap<>();
        } catch (IOException | RuntimeException e) {
            system.logger.error("Failed to read config: " + e.getMessage());
            return new HashMap<>();
        }
    }

    private static void writeConfig(Map<String, Object> data) {
        File file = getConfigFile();
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            system.logger.error("Failed to write config: " + e.getMessage());
        }
    }

    public static <T, O> void addValue(T key, O value) {
        Map<String, Object> data = readConfig();
        data.put(String.valueOf(key), value);
        writeConfig(data);
    }

    public static <T, O> void setValue(T key, O value) {
        Map<String, Object> data = readConfig();
        String k = String.valueOf(key);
        if (data.containsKey(k)) {
            data.put(k, value);
            writeConfig(data);
        } else {
            system.logger.error("Key '" + k + "' not found. Use addValue instead.");
        }
    }

    public static boolean has(String key) {
        return readConfig().containsKey(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getValue(String key) {
        Object value = readConfig().get(key);
        if (value == null) return null;

        // If the value is already of the right type (as far as we can guess)
        try {
            return (T) value;
        } catch (ClassCastException ignored) {
            // We'll try to handle conversions below
        }

        if (value instanceof Number num) {
            Object converted = num;

            if (num instanceof Double d) {
                if (Math.floor(d) == d) converted = (int) d.doubleValue();
            }

            try {
                return (T) converted;
            } catch (Exception ignored) {}
        }

        try {
            return (T) gson.fromJson(gson.toJsonTree(value), Object.class);
        } catch (Exception e) {
            system.logger.error("Failed to cast key '" + key + "': " + e.getMessage());
            return null;
        }
    }


    public static boolean remove(String key) {
        Map<String, Object> data = readConfig();
        if (data.remove(key) != null) {
            writeConfig(data);
            return true;
        }
        return false;
    }

    public static void init() {
        if (!has("isFirstSeen")) {
            addValue("isFirstSeen", true);
        }
    }
}
