package me.khalit.projectleviathan.utils;

import me.khalit.projectleviathan.data.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class Util {

    public static String fixColors(String raw) {
        return ChatColor.translateAlternateColorCodes('&', raw);
    }

    public static List<String> fixColors(List<String> strings) {
        return strings.stream().map(Util::fixColors)
                .collect(Collectors.toList());
    }

    public static String[] fixColors(String[] strings) {
        String[] fixed = new String[strings.length];
        for (int i = 0; i < strings.length; i++) {
            fixed[i] = fixColors(strings[i]);
        }
        return fixed;
    }

    public static void sendMessage(Player player, String text) {
        player.sendMessage(fixColors(text));
    }

    public static void sendMessage(CommandSender sender, String text) {
        sender.sendMessage(fixColors(text));
    }

    public static void sendMessage(User user, String text) {
        user.getPlayer().sendMessage(fixColors(text));
    }

    public static void sendList(Player player, List<String> list) {
        for (String string : list) {
            sendMessage(player, string);
        }
    }

    public static void sendList(CommandSender sender, List<String> list) {
        for (String string : list) {
            sendMessage(sender, string);
        }
    }

    public static void sendAllList(List<String> list) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (String string : list) {
                sendMessage(player, string);
            }
        }
    }

    public static void sendAllMessage(String text) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendMessage(player, text);
        }
    }

}
