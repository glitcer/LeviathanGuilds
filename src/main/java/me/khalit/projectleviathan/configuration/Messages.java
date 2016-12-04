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

    private static final String LOCALE = Settings.getString("locale");

    public static List<String> getMultiple(String key) {
        return Locale.fileByLocale(LOCALE).multipleKeys.get(key);
    }

    public static String getSingle(String locale, String key) {
        return Locale.fileByLocale(LOCALE).singleKeys.get(key);
    }

}
