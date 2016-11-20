package me.khalit.projectleviathan.configuration;

import me.khalit.projectleviathan.Main;
import me.khalit.projectleviathan.data.User;
import me.khalit.projectleviathan.data.managers.UserManager;
import me.khalit.projectleviathan.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Messages {

    private static HashMap<String, HashMap<String, String>> singleKeys = new HashMap<>();
    private static HashMap<String, HashMap<String, List<String>>> multipleKeys = new HashMap<>();

    public Messages() {
        for (ConcurrentConfigurableFile file : Locale.getConfigurableFiles()) {
            FileConfiguration messages = file.getFileConfiguration();
            for (String key : messages.getKeys(true)) {
                if (key.toLowerCase().endsWith("list")) {
                    List<String> list = messages.getStringList(key);
                    if (list == null) {
                        continue;
                    }
                    HashMap<String, List<String>> keys = new HashMap<>();
                    keys.put(key, Util.fixColors(list));
                    multipleKeys.put(Locale.localeByConfigurableFile(file), keys);
                    continue;
                }
                HashMap<String, String> keys = new HashMap<>();
                keys.put(key, Util.fixColors(messages.getString(key)));
                singleKeys.put(Locale.localeByConfigurableFile(file), keys);
            }
        }
    }

    public static List<String> getMultiple(String locale, String key) {
        for (String find : multipleKeys.keySet()) {
            if (find.equals(locale)) {
                HashMap<String, List<String>> keys = multipleKeys.get(find);
                for (String k : keys.keySet()) {
                    if (k.equals(key)) {
                        return keys.get(k);
                    }
                }
            }
        }
        return Collections.singletonList("key can not be null");
    }

    public static String getSingle(String locale, String key) {
        for (String find : singleKeys.keySet()) {
            if (find.equals(locale)) {
                HashMap<String, String> keys = singleKeys.get(find);
                for (String k : keys.keySet()) {
                    if (k.equals(key)) {
                        return keys.get(k);
                    }
                }
            }
        }
        return "key can not be null";
    }

    public static void single(User user, String key) {
        Util.sendMessage(user, getSingle(Locale.localeByConfigurableFile(user.getLocale()), key));
    }

    public static void multiple(User user, String key) {
        for (String str : getMultiple(Locale.localeByConfigurableFile(user.getLocale()), key)) {
            Util.sendMessage(user, str);
        }
    }

    public static void broadcastSingle(String key) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            User u = UserManager.getUser(p);
            single(u, key);
        }
    }

    public static void broadcastMultiple(String key) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            User u = UserManager.getUser(p);
            multiple(u, key);
        }
    }

}
