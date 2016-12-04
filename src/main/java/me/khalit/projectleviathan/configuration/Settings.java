package me.khalit.projectleviathan.configuration;

import me.khalit.projectleviathan.Main;
import me.khalit.projectleviathan.utils.Util;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Settings {

    private static final FileConfiguration CONFIG = Main.getInstance().getConfig();

    public static List<String> getStringList(String key) {
        return CONFIG.getStringList(key);
    }

    public static String getString(String key) {
        return CONFIG.getString(key);
    }

    public static int getInt(String key) {
        return CONFIG.getInt(key);
    }

    public static long getLong(String key) {
        return CONFIG.getLong(key);
    }

    public static short getShort(String key) {
        return (short) CONFIG.getInt(key);
    }

    public static Object get(String key) {
        return CONFIG.get(key);
    }

    public static boolean getBoolean(String key) {
        return CONFIG.getBoolean(key);
    }


}
