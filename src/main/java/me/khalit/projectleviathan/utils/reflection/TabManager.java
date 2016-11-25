package me.khalit.projectleviathan.utils.reflection;

import me.khalit.projectleviathan.configuration.Settings;
import me.khalit.projectleviathan.configuration.TabReader;
import me.khalit.projectleviathan.data.User;
import me.khalit.projectleviathan.data.managers.UserManager;
import me.khalit.projectleviathan.utils.KeyPair;
import me.khalit.projectleviathan.utils.Parser;
import me.khalit.projectleviathan.utils.Util;
import me.khalit.projectleviathan.utils.runnables.TPSMonitor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Calendar;

public class TabManager {

    public static void updateLight(Player player) {
        ProtocolManager.getTabPacket().sendPacketHeaderFooter(player,
                Settings.getString("tab.header"),
                Settings.getString("tab.footer"));

        for (KeyPair<Integer, Integer> entry : TabReader.getUpdateSlots()) {
            int column = entry.getValueFirst() - 1;
            int row = entry.getValueSecond() - 1;

            KeyPair<Integer, Integer> slot = new KeyPair<>();
            slot.put(column, row);

            String str = TabReader.getSlots().get(slot) == null ? "" : TabReader.getSlots().get(slot);
            TabExecutor.update(player, row, column,
                    replaceVariables(player, str));
        }
        TabExecutor.execute(player);
    }

    public static void updateHeavy(Player player) {
        ProtocolManager.getTabPacket().sendPacketHeaderFooter(player,
                Settings.getString("tab.header"),
                Settings.getString("tab.footer"));

        for (int column = 0; column < 4; column++) {
            for (int row = 0; row < 20; row++) {
                String str = "";
                for (KeyPair<Integer, Integer> entry : TabReader.getSlots().keySet()) {
                    if (entry.getValueFirst().equals(column)
                            && entry.getValueSecond().equals(row)) {
                        str = TabReader.getSlots().get(entry);
                    }
                }
                TabExecutor.update(player, row, column,
                        replaceVariables(player, str));
            }
        }
    }


    public static void show(Player player) {
        ProtocolManager.getTabPacket().sendPacketHeaderFooter(player,
                Settings.getString("tab.header"),
                Settings.getString("tab.footer"));

        for (int column = 0; column < 4; column++) {
            for (int row = 0; row < 20; row++) {
                String str = "";
                for (KeyPair<Integer, Integer> entry : TabReader.getSlots().keySet()) {
                    if (entry.getValueFirst().equals(column)
                            && entry.getValueSecond().equals(row)) {
                        str = TabReader.getSlots().get(entry);
                    }
                }
                TabExecutor.set(player, row, column,
                        replaceVariables(player, str));
            }
        }

        TabExecutor.execute(player);
    }

    public static String replaceVariables(Player player, String string) {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        User user = UserManager.getUser(player);
        string = StringUtils.replace(string, "{PLAYER}", player.getName());
        string = StringUtils.replace(string, "{DISPLAYNAME}", player.getDisplayName());

        string = StringUtils.replace(string, "{FOOD}", String.valueOf(player.getFoodLevel()));
        string = StringUtils.replace(string, "{HEALTH}", String.valueOf(player.getHealth()));
        if (hour > 10) string = StringUtils.replace(string, "{HOUR}", String.valueOf(hour));
        else string = StringUtils.replace(string, "{HOUR}", "0" + String.valueOf(hour));
        if (minute > 10) string = StringUtils.replace(string, "{MINUTE}", String.valueOf(minute));
        else string = StringUtils.replace(string, "{MINUTE}", "0" + String.valueOf(minute));
        if (second > 10) string = StringUtils.replace(string, "{SECOND}", String.valueOf(second));
        else string = StringUtils.replace(string, "{SECOND}", "0" + String.valueOf(second));
        string = StringUtils.replace(string, "{DATE}", Parser.parseTime(System.currentTimeMillis()));
        string = StringUtils.replace(string, "{EXP}", String.valueOf(Util.round(player.getExp(), 1)));
        string = StringUtils.replace(string, "{EXP-TO-LVLUP}", String.valueOf(player.getExpToLevel()));
        string = StringUtils.replace(string, "{LEVEL}", String.valueOf(player.getLevel()));
        string = StringUtils.replace(string, "{TOTAL-EXP}", String.valueOf(player.getTotalExperience()));
        string = StringUtils.replace(string, "{WORLD}", player.getWorld().getName());
        string = StringUtils.replace(string, "{ONLINE}", String.valueOf(Bukkit.getOnlinePlayers().size()));
        string = StringUtils.replace(string, "{TPS}", String.valueOf(TPSMonitor.getTPS()));
        string = StringUtils.replace(string, "{PING}", String.valueOf(user.getPing()));
        return Util.fixColors(string);
    }

}
