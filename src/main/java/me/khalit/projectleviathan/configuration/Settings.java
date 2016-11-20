package me.khalit.projectleviathan.configuration;

import me.khalit.projectleviathan.Main;
import me.khalit.projectleviathan.utils.Util;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Settings {

    private static HashMap<String, Object> singleKeys = new HashMap<>();
    private static HashMap<String, List<String>> multipleKeys = new HashMap<>();

    public Settings() {
        FileConfiguration config = Main.getInstance().getConfig();
        for (String key : config.getKeys(true)) {
            if (key.toLowerCase().endsWith("list")) {
                List<String> list = config.getStringList(key);
                if (list == null) {
                    continue;
                }
                multipleKeys.put(key, Util.fixColors(list));
                continue;
            }
            singleKeys.put(key, config.get(key));
        }
    }

    public static List<String> getStringList(String key) {
        for (String find : multipleKeys.keySet()) {
            if (find.equals(key)) return multipleKeys.get(key);
        }
        return Collections.singletonList("key can not be null");
    }

    public static String getString(String key) {
        for (String find : singleKeys.keySet()) {
            if (find.equals(key)) return (String)singleKeys.get(key);
        }
        return "key can not be null";
    }

    public static int getInt(String key) {
        for (String find : singleKeys.keySet()) {
            if (find.equals(key)) return (int)singleKeys.get(key);
        }
        return 0;
    }

    public static long getLong(String key) {
        for (String find : singleKeys.keySet()) {
            if (find.equals(key)) return (long)singleKeys.get(key);
        }
        return 0;
    }

    public static short getShort(String key) {
        for (String find : singleKeys.keySet()) {
            if (find.equals(key)) return (short)singleKeys.get(key);
        }
        return 0;
    }

    public static Object get(String key) {
        for (String find : singleKeys.keySet()) {
            if (find.equals(key)) return singleKeys.get(key);
        }
        return null;
    }

    public static boolean getBoolean(String key) {
        for (String find : singleKeys.keySet()) {
            if (find.equals(key)) return (boolean)singleKeys.get(key);
        }
        return false;
    }


}
