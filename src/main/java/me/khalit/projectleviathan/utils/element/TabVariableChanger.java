package me.khalit.projectleviathan.utils.element;

import me.khalit.projectleviathan.data.User;
import me.khalit.projectleviathan.data.managers.UserManager;
import me.khalit.projectleviathan.utils.Parser;
import me.khalit.projectleviathan.utils.Util;
import me.khalit.projectleviathan.utils.runnables.TPSMonitor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Calendar;

public class TabVariableChanger {

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
